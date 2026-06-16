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

import com.palantir.conjure.python.poet.ErrorSnippet;
import com.palantir.conjure.python.poet.PythonField;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonPackage;
import com.palantir.conjure.python.processors.packagename.PackageNameProcessor;
import com.palantir.conjure.python.processors.typename.TypeNameProcessor;
import com.palantir.conjure.spec.ErrorDefinition;
import com.palantir.conjure.spec.FieldDefinition;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        Set<PythonImport> imports = Stream.concat(errorDef.getSafeArgs().stream(), errorDef.getUnsafeArgs().stream())
                .flatMap(entry -> entry.getType().accept(importVisitor).stream())
                .collect(Collectors.toSet());

        List<PythonField> safeArgs = toPythonFields(errorDef.getSafeArgs());
        List<PythonField> unsafeArgs = toPythonFields(errorDef.getUnsafeArgs());

        ErrorSnippet.Builder builder = ErrorSnippet.builder()
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
                .unsafeArgs(unsafeArgs);

        if (!safeArgs.isEmpty() || !unsafeArgs.isEmpty()) {
            builder.addImports(ErrorSnippet.TYPED_DICT_IMPORT);
        }

        return builder.build();
    }

    private List<PythonField> toPythonFields(List<FieldDefinition> fields) {
        return fields.stream()
                .map(field -> PythonTypeGenerator.generateField(
                        field, pythonTypeNameVisitor, myPyTypeNameVisitor, dealiasingTypeVisitor))
                .collect(Collectors.toList());
    }
}
