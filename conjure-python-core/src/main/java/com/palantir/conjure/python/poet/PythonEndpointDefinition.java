/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.conjure.python.poet;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.spec.AuthType;
import com.palantir.conjure.spec.Documentation;
import com.palantir.conjure.spec.HeaderParameterType;
import com.palantir.conjure.spec.HttpMethod;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ParameterId;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.visitor.AuthTypeVisitor;
import com.palantir.conjure.visitor.ParameterTypeVisitor;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonEndpointDefinition extends Emittable {

    String pythonMethodName();

    HttpMethod httpMethod();

    HttpPath httpPath();

    Optional<AuthType> auth();

    Optional<Documentation> docs();

    List<PythonEndpointParam> params();

    boolean isBinary();

    boolean isOptionalReturnType();

    Optional<String> pythonReturnType();

    Optional<String> myPyReturnType();

    @Value.Check
    default void check() {
        checkState(pythonReturnType().isPresent() == myPyReturnType().isPresent(),
                "expected both return types or neither");
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            // if auth type is header, insert it as a fake param
            boolean isHeaderType = auth().isPresent() && auth().get().accept(AuthTypeVisitor.IS_HEADER);
            List<PythonEndpointParam> paramsWithHeader = isHeaderType
                    ? ImmutableList.<PythonEndpointParam>builder()
                    .add(PythonEndpointParam.builder()
                            .paramName("authHeader")
                            .pythonParamName("auth_header")
                            .myPyType("str")
                            .isOptional(false)
                            .paramType(ParameterType.header(HeaderParameterType.of(ParameterId.of("Authorization"))))
                            .build())
                    .addAll(params())
                    .build() : params();

            poetWriter.writeIndentedLine("def %s(self, %s):",
                    pythonMethodName(),
                    Joiner.on(", ").join(
                            paramsWithHeader.stream()
                                    .sorted(new PythonEndpointParamComparator())
                                    .map(param -> {
                                        if (param.isOptional()) {
                                            return String.format("%s=None", param.pythonParamName());
                                        }
                                        return param.pythonParamName();
                                    })
                                    .collect(Collectors.toList())));
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("# type: (%s) -> %s",
                    Joiner.on(", ").join(
                            paramsWithHeader.stream()
                                    .sorted(new PythonEndpointParamComparator())
                                    .map(PythonEndpointParam::myPyType)
                                    .collect(Collectors.toList())),
                    myPyReturnType().orElse("None"));
            docs().ifPresent(docs -> {
                poetWriter.writeIndentedLine("\"\"\"");
                poetWriter.writeIndentedLine(docs.get().trim());
                poetWriter.writeIndentedLine("\"\"\"");
            });

            // header
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("_headers = {");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("'Accept': '%s',",
                    isBinary() ? MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_JSON);

            // body
            Optional<PythonEndpointParam> bodyParam = paramsWithHeader.stream()
                    .filter(param -> param.paramType().accept(ParameterTypeVisitor.IS_BODY))
                    .findAny();
            // TODO(forozco): handle non-json body
            if (bodyParam.isPresent()) {
                poetWriter.writeIndentedLine("'Content-Type': 'application/json',");
            }
            paramsWithHeader.stream()
                    .filter(param -> param.paramType().accept(ParameterTypeVisitor.IS_HEADER))
                    .forEach(param -> {
                        poetWriter.writeIndentedLine("'%s': %s,",
                                param.paramType().accept(ParameterTypeVisitor.HEADER)
                                        .getParamId().get(), param.pythonParamName());
                    });
            poetWriter.decreaseIndent();
            poetWriter.writeIndentedLine("} # type: Dict[str, Any]");

            // params
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("_params = {");
            poetWriter.increaseIndent();
            paramsWithHeader.stream()
                    .filter(param -> param.paramType().accept(ParameterTypeVisitor.IS_QUERY))
                    .forEach(param -> {
                        poetWriter.writeIndentedLine("'%s': %s,",
                                param.paramType().accept(ParameterTypeVisitor.QUERY).getParamId().get(),
                                param.pythonParamName());
                    });
            poetWriter.decreaseIndent();
            poetWriter.writeIndentedLine("} # type: Dict[str, Any]");

            // path params
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("_path_params = {");
            poetWriter.increaseIndent();
            // TODO(qchen): no need for param name twice?
            paramsWithHeader.stream()
                    .filter(param -> param.paramType().accept(ParameterTypeVisitor.IS_PATH))
                    .forEach(param -> {
                        poetWriter.writeIndentedLine("'%s': %s,",
                                param.paramName(),
                                param.pythonParamName());
                    });
            poetWriter.decreaseIndent();
            poetWriter.writeIndentedLine("} # type: Dict[str, Any]");

            if (bodyParam.isPresent()) {
                poetWriter.writeLine();
                poetWriter.writeIndentedLine("_json = ConjureEncoder().default(%s) # type: Any",
                        bodyParam.get().pythonParamName());
            } else {
                poetWriter.writeLine();
                poetWriter.writeIndentedLine("_json = None # type: Any");
            }

            // fix the path, add path params
            poetWriter.writeLine();

            HttpPath fullPath = httpPath();
            String fixedPath = fullPath.toString().replaceAll("\\{(.*):[^}]*\\}", "{$1}");
            poetWriter.writeIndentedLine("_path = '%s'", fixedPath);
            poetWriter.writeIndentedLine("_path = _path.format(**_path_params)");

            poetWriter.writeLine();
            poetWriter.writeIndentedLine("_response = self._request( # type: ignore");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("'%s',", httpMethod());
            poetWriter.writeIndentedLine("self._uri + _path,");
            poetWriter.writeIndentedLine("params=_params,");
            poetWriter.writeIndentedLine("headers=_headers,");
            if (isBinary()) {
                poetWriter.writeIndentedLine("stream=True,");
            }
            poetWriter.writeIndentedLine("json=_json)");
            poetWriter.decreaseIndent();

            poetWriter.writeLine();
            if (isBinary()) {
                poetWriter.writeIndentedLine("_raw = _response.raw");
                poetWriter.writeIndentedLine("_raw.decode_content = True");
                poetWriter.writeIndentedLine("return _raw");
            } else if (pythonReturnType().isPresent()) {
                poetWriter.writeIndentedLine("_decoder = ConjureDecoder()");
                if (isOptionalReturnType()) {
                    poetWriter.writeIndentedLine(
                            "return None if _response.status_code == 204 else _decoder.decode(_response.json(), %s)",
                            pythonReturnType().get());
                } else {
                    poetWriter.writeIndentedLine("return _decoder.decode(_response.json(), %s)",
                            pythonReturnType().get());
                }
            } else {
                poetWriter.writeIndentedLine("return");
            }

            poetWriter.decreaseIndent();
        });
    }

    class Builder extends ImmutablePythonEndpointDefinition.Builder {}

    static Builder builder() {
        return new Builder();
    }

    @Value.Immutable
    public interface PythonEndpointParam {

        String paramName();

        String pythonParamName();

        String myPyType();

        ParameterType paramType();

        boolean isOptional();

        class Builder extends ImmutablePythonEndpointParam.Builder {}

        static Builder builder() {
            return new Builder();
        }

    }

    class PythonEndpointParamComparator implements Comparator<PythonEndpointParam> {
        @Override
        public int compare(PythonEndpointParam o1, PythonEndpointParam o2) {
            if (o1.isOptional() && !o2.isOptional()) {
                return 1;
            }
            if (!o1.isOptional() && o2.isOptional()) {
                return -1;
            }
            return o1.pythonParamName().compareTo(o2.pythonParamName());
        }
    }

}
