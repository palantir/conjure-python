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

import com.palantir.conjure.python.types.ImportTypeVisitor;
import com.palantir.conjure.spec.Documentation;
import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonService extends PythonSnippet {
    PythonImport CONJURE_IMPORT = PythonImport.builder()
            .moduleSpecifier(ImportTypeVisitor.CONJURE_PYTHON_CLIENT)
            .addNamedImports("Service", "ConjureEncoder", "ConjureDecoder")
            .build();

    @Override
    @Value.Default
    default String idForSorting() {
        return className();
    }

    String className();

    Optional<Documentation> docs();

    List<PythonEndpointDefinition> endpointDefinitions();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            poetWriter.writeIndentedLine(String.format("class %s(Service):", className()));
            poetWriter.increaseIndent();
            docs().ifPresent(docs -> poetWriter.writeIndentedLine(String.format("\"\"\"%s\"\"\"", docs.get().trim())));

            endpointDefinitions().forEach(endpointDefinition -> {
                poetWriter.writeLine();
                endpointDefinition.emit(poetWriter);
            });
            poetWriter.writeLine();

            poetWriter.decreaseIndent();
        });
    }

    class Builder extends ImmutablePythonService.Builder {}

    static Builder builder() {
        return new Builder();
    }

}
