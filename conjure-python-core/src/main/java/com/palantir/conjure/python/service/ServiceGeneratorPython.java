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

package com.palantir.conjure.python.service;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.palantir.conjure.python.PackageNameProcessor;
import com.palantir.conjure.python.PythonFileGenerator;
import com.palantir.conjure.python.poet.PythonEndpointDefinition;
import com.palantir.conjure.python.poet.PythonEndpointDefinition.PythonEndpointParam;
import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonService;
import com.palantir.conjure.python.types.DefaultTypeNameVisitor;
import com.palantir.conjure.python.types.MyPyTypeNameVisitor;
import com.palantir.conjure.python.types.TypeMapper;
import com.palantir.conjure.python.util.CaseConverter;
import com.palantir.conjure.python.util.ImportsVisitor;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.TypeVisitor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ServiceGeneratorPython implements PythonFileGenerator<ServiceDefinition> {

    @Override
    public PythonFile generateFile(
            Map<TypeName, TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            ServiceDefinition serviceDefinition) {

        ImportsVisitor importsVisitor = new ImportsVisitor(
                serviceDefinition.getServiceName(), packageNameProcessor, types);
        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types.keySet()));
        TypeMapper myPyMapper = new TypeMapper(new MyPyTypeNameVisitor(types.keySet()));
        String packageName = packageNameProcessor.getPackageName(serviceDefinition.getServiceName().getPackage());

        Builder<Type> referencedTypesBuilder = ImmutableSet.builder();

        List<PythonEndpointDefinition> endpoints = serviceDefinition.getEndpoints()
                .stream()
                .map(ed -> {
                    ed.getReturns().ifPresent(referencedTypesBuilder::add);
                    ed.getArgs().forEach(arg -> referencedTypesBuilder.add(arg.getType()));

                    List<PythonEndpointParam> params = ed.getArgs()
                            .stream()
                            .map(argEntry -> PythonEndpointParam
                                    .builder()
                                    .paramName(argEntry.getArgName().get())
                                    .pythonParamName(CaseConverter.toCase(
                                            argEntry.getArgName().get(), CaseConverter.Case.SNAKE_CASE))
                                    .paramType(argEntry.getParamType())
                                    .myPyType(myPyMapper.getTypeName(argEntry.getType()))
                                    .build())
                            .collect(Collectors.toList());

                    return PythonEndpointDefinition.builder()
                            .pythonMethodName(CaseConverter.toCase(
                                    ed.getEndpointName().get(), CaseConverter.Case.SNAKE_CASE))
                            .httpMethod(ed.getHttpMethod())
                            .httpPath(ed.getHttpPath())
                            .auth(ed.getAuth())
                            .docs(ed.getDocs())
                            .params(params)
                            .pythonReturnType(ed.getReturns().map(mapper::getTypeName))
                            .myPyReturnType(ed.getReturns().map(myPyMapper::getTypeName))
                            .isBinary(ed.getReturns().map(rt -> {
                                if (rt.accept(TypeVisitor.IS_PRIMITIVE)) {
                                    return rt.accept(TypeVisitor.PRIMITIVE).get() == PrimitiveType.Value.BINARY;
                                }
                                return false;
                            }).orElse(false))
                            .build();
                })
                .collect(Collectors.toList());

        List<PythonImport> imports = referencedTypesBuilder.build()
                .stream()
                .flatMap(entry -> entry.accept(importsVisitor).stream())
                .collect(Collectors.toList());

        return PythonFile.builder()
                .fileName(String.format("%s.py", serviceDefinition.getServiceName().getName()))
                .packageName(packageName)
                .imports(imports)
                .addContents(PythonService.builder()
                        .className(serviceDefinition.getServiceName().getName())
                        .docs(serviceDefinition.getDocs())
                        .addAllEndpointDefinitions(endpoints)
                        .build())
                .build();
    }
}
