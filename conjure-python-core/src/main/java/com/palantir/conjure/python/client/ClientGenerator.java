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
import com.palantir.conjure.CaseConverter;
import com.palantir.conjure.python.poet.PythonEndpointDefinition;
import com.palantir.conjure.python.poet.PythonEndpointDefinition.PythonEndpointParam;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonService;
import com.palantir.conjure.python.poet.PythonSnippet;
import com.palantir.conjure.python.types.ImportTypeVisitor;
import com.palantir.conjure.python.types.PythonTypeVisitor;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ClientGenerator {

    public PythonSnippet generateClient(
            ServiceDefinition serviceDef,
            Function<TypeName, ImportTypeVisitor> importTypeVisitorFactory,
            DealiasingTypeVisitor dealiasingTypeVisitor) {
        ImportTypeVisitor importTypeVisitor = importTypeVisitorFactory.apply(serviceDef.getServiceName());
        ImmutableSet.Builder<Type> referencedTypesBuilder = ImmutableSet.builder();

        List<PythonEndpointDefinition> endpoints = serviceDef.getEndpoints()
                .stream()
                .map(endpointDef -> generateEndpoint(endpointDef, referencedTypesBuilder, dealiasingTypeVisitor))
                .collect(Collectors.toList());

        List<PythonImport> imports = referencedTypesBuilder.build()
                .stream()
                .flatMap(entry -> entry.accept(importTypeVisitor).stream())
                .collect(Collectors.toList());

        return PythonService.builder()
                .name(serviceDef.getServiceName().getName())
                .addImports(PythonService.CONJURE_IMPORT)
                .addAllImports(imports)
                .docs(serviceDef.getDocs())
                .addAllEndpointDefinitions(endpoints)
                .build();
    }

    private PythonEndpointDefinition generateEndpoint(
            EndpointDefinition endpointDef,
            ImmutableSet.Builder<Type> referencedTypesBuilder,
            DealiasingTypeVisitor dealiasingTypeVisitor) {

        endpointDef.getReturns().ifPresent(referencedTypesBuilder::add);
        endpointDef.getArgs().forEach(arg -> referencedTypesBuilder.add(arg.getType()));

        List<PythonEndpointParam> params = endpointDef.getArgs()
                .stream()
                .map(argEntry -> PythonEndpointParam
                        .builder()
                        .paramName(argEntry.getArgName().get())
                        .pythonParamName(CaseConverter.toCase(
                                argEntry.getArgName().get(), CaseConverter.Case.SNAKE_CASE))
                        .paramType(argEntry.getParamType())
                        .myPyType(argEntry.getType().accept(PythonTypeVisitor.MY_PY_TYPE))
                        .isOptional(dealiasingTypeVisitor.dealias(argEntry.getType()).fold(
                                typeDefinition -> false,
                                type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                        .build())
                .collect(Collectors.toList());

        return PythonEndpointDefinition.builder()
                .pythonMethodName(CaseConverter.toCase(
                        endpointDef.getEndpointName().get(), CaseConverter.Case.SNAKE_CASE))
                .httpMethod(endpointDef.getHttpMethod())
                .httpPath(endpointDef.getHttpPath())
                .auth(endpointDef.getAuth())
                .docs(endpointDef.getDocs())
                .params(params)
                .pythonReturnType(endpointDef.getReturns().map(type -> type.accept(PythonTypeVisitor.PYTHON_TYPE)))
                .myPyReturnType(endpointDef.getReturns().map(type -> type.accept(PythonTypeVisitor.MY_PY_TYPE)))
                .isBinary(endpointDef.getReturns()
                        .map(rt -> rt.accept(TypeVisitor.IS_PRIMITIVE)
                                && rt.accept(TypeVisitor.PRIMITIVE).get() == PrimitiveType.Value.BINARY)
                        .orElse(false))
                .isOptionalReturnType(endpointDef.getReturns()
                        .map(rt -> dealiasingTypeVisitor.dealias(rt).fold(
                                typeDefinition -> false,
                                type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                        .orElse(false))
                .build();
    }
}
