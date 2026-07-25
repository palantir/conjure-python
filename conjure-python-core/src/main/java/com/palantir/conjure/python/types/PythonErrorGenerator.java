/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.python.types;

import com.palantir.conjure.CaseConverter;
import com.palantir.conjure.python.poet.ErrorSnippet;
import com.palantir.conjure.python.poet.PythonField;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonPackage;
import com.palantir.conjure.python.processors.packagename.PackageNameProcessor;
import com.palantir.conjure.python.processors.typename.TypeNameProcessor;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class PythonErrorGenerator {

    private final PackageNameProcessor implPackageNameProcessor;
    private final TypeNameProcessor implTypeNameProcessor;
    private final PackageNameProcessor definitionPackageNameProcessor;
    private final TypeNameProcessor definitionTypeNameProcessor;
    private final DealiasingTypeVisitor dealiasingTypeVisitor;
    private final PythonTypeNameVisitor pythonTypeNameVisitor;
    private final MyPyTypeNameVisitor myPyTypeNameVisitor;

    public PythonErrorGenerator(
            PackageNameProcessor implPackageNameProcessor,
            TypeNameProcessor implTypeNameProcessor,
            PackageNameProcessor definitionPackageNameProcessor,
            TypeNameProcessor definitionTypeNameProcessor,
            DealiasingTypeVisitor dealiasingTypeVisitor) {
        this.implPackageNameProcessor = implPackageNameProcessor;
        this.implTypeNameProcessor = implTypeNameProcessor;
        this.definitionPackageNameProcessor = definitionPackageNameProcessor;
        this.definitionTypeNameProcessor = definitionTypeNameProcessor;
        this.dealiasingTypeVisitor = dealiasingTypeVisitor;
        this.pythonTypeNameVisitor = new PythonTypeNameVisitor(implTypeNameProcessor);
        this.myPyTypeNameVisitor = new MyPyTypeNameVisitor(dealiasingTypeVisitor, implTypeNameProcessor);
    }

    public ErrorSnippet generateError(ErrorDefinition errorDef) {
        ImportTypeVisitor importVisitor =
                new ImportTypeVisitor(errorDef.getErrorName(), implTypeNameProcessor, implPackageNameProcessor);

        // Collect imports from all field types
        Set<PythonImport> imports = errorDef.getSafeArgs().stream()
                .flatMap(entry -> entry.getType().accept(importVisitor).stream())
                .collect(Collectors.toSet());
        imports.addAll(errorDef.getUnsafeArgs().stream()
                .flatMap(entry -> entry.getType().accept(importVisitor).stream())
                .collect(Collectors.toSet()));

        // Convert safe args to PythonFields
        List<PythonField> safeArgs = errorDef.getSafeArgs().stream()
                .map(entry -> PythonField.builder()
                        .attributeName(CaseConverter.toCase(entry.getFieldName().get(), CaseConverter.Case.SNAKE_CASE))
                        .jsonIdentifier(entry.getFieldName().get())
                        .docs(entry.getDocs())
                        .pythonType(entry.getType().accept(pythonTypeNameVisitor))
                        .myPyType(entry.getType().accept(myPyTypeNameVisitor))
                        .isOptional(dealiasingTypeVisitor
                                .dealias(entry.getType())
                                .fold(_typeDefinition -> false, type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                        .build())
                .collect(Collectors.toList());

        // Convert unsafe args to PythonFields
        List<PythonField> unsafeArgs = errorDef.getUnsafeArgs().stream()
                .map(entry -> PythonField.builder()
                        .attributeName(CaseConverter.toCase(entry.getFieldName().get(), CaseConverter.Case.SNAKE_CASE))
                        .jsonIdentifier(entry.getFieldName().get())
                        .docs(entry.getDocs())
                        .pythonType(entry.getType().accept(pythonTypeNameVisitor))
                        .myPyType(entry.getType().accept(myPyTypeNameVisitor))
                        .isOptional(dealiasingTypeVisitor
                                .dealias(entry.getType())
                                .fold(_typeDefinition -> false, type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                        .build())
                .collect(Collectors.toList());

        return ErrorSnippet.builder()
                .pythonPackage(PythonPackage.of(
                        implPackageNameProcessor.process(errorDef.getErrorName().getPackage())))
                .className(implTypeNameProcessor.process(errorDef.getErrorName()))
                .definitionPackage(PythonPackage.of(definitionPackageNameProcessor.process(
                        errorDef.getErrorName().getPackage())))
                .definitionName(definitionTypeNameProcessor.process(errorDef.getErrorName()))
                .addAllImports(ErrorSnippet.DEFAULT_IMPORTS)
                .addAllImports(imports)
                .docs(errorDef.getDocs())
                .errorCode(errorDef.getCode().toString())
                .namespace(errorDef.getNamespace().toString())
                .safeArgs(safeArgs)
                .unsafeArgs(unsafeArgs)
                .build();
    }
}
