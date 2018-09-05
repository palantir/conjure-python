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

package com.palantir.conjure.python.client;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.palantir.conjure.python.PackageNameProcessor;
import com.palantir.conjure.python.poet.PythonClass;
import com.palantir.conjure.python.poet.PythonClassName;
import com.palantir.conjure.python.poet.PythonEndpointDefinition;
import com.palantir.conjure.python.poet.PythonEndpointDefinition.PythonEndpointParam;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonService;
import com.palantir.conjure.python.types.DefaultTypeNameVisitor;
import com.palantir.conjure.python.types.MyPyTypeNameVisitor;
import com.palantir.conjure.python.types.ReferencedTypeNameVisitor;
import com.palantir.conjure.python.types.TypeMapper;
import com.palantir.conjure.python.util.CaseConverter;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClientGenerator {

    public PythonClass generateClient(
            List<TypeDefinition> types,
            PackageNameProcessor packageNameProvider,
            ServiceDefinition serviceDefinition) {

        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types));
        TypeMapper myPyMapper = new TypeMapper(new MyPyTypeNameVisitor(types));

        DealiasingTypeVisitor dealiasingTypeVisitor = new DealiasingTypeVisitor(types.stream()
                .collect(Collectors.toMap(type -> type.accept(TypeDefinitionVisitor.TYPE_NAME), Function.identity())));
        ReferencedTypeNameVisitor referencedTypeNameVisitor = new ReferencedTypeNameVisitor(types, packageNameProvider);

        Builder<PythonClassName> referencedTypesBuilder = ImmutableSet.builder();

        List<PythonEndpointDefinition> endpoints = serviceDefinition.getEndpoints()
                .stream()
                .map(ed -> {
                    ed.getReturns()
                            .ifPresent(returnType -> referencedTypesBuilder.addAll(
                                    returnType.accept(referencedTypeNameVisitor)));
                    ed.getArgs().forEach(arg -> referencedTypesBuilder.addAll(
                            arg.getType().accept(referencedTypeNameVisitor)));

                    List<PythonEndpointParam> params = ed.getArgs()
                            .stream()
                            .map(argEntry -> PythonEndpointParam
                                    .builder()
                                    .paramName(argEntry.getArgName().get())
                                    .pythonParamName(CaseConverter.toCase(
                                            argEntry.getArgName().get(), CaseConverter.Case.SNAKE_CASE))
                                    .paramType(argEntry.getParamType())
                                    .myPyType(myPyMapper.getTypeName(argEntry.getType()))
                                    .isOptional(dealiasingTypeVisitor.dealias(argEntry.getType()).fold(
                                            typeDefinition -> false,
                                            type -> type.accept(TypeVisitor.IS_OPTIONAL)))
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
                            .isOptionalReturnType(ed.getReturns()
                                    .map(rt -> dealiasingTypeVisitor.dealias(rt).fold(
                                            typeDefinition -> false,
                                            type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                                    .orElse(false))
                            .build();
                })
                .collect(Collectors.toList());

        String packageName =
                packageNameProvider.getPackageName(serviceDefinition.getServiceName().getPackage());
        List<PythonImport> imports = referencedTypesBuilder.build()
                .stream()
                .filter(entry -> !entry.conjurePackage().equals(packageName)) // don't need to import if in this file
                .map(className -> PythonImport.of(className, packageName))
                .collect(Collectors.toList());

        return PythonService.builder()
                .packageName(packageName)
                .addAllRequiredImports(PythonService.DEFAULT_IMPORTS)
                .addAllRequiredImports(imports)
                .className(serviceDefinition.getServiceName().getName())
                .docs(serviceDefinition.getDocs())
                .addAllEndpointDefinitions(endpoints)
                .build();
    }
}
