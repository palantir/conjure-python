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

package com.palantir.conjure.python.types;

import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.CaseConverter;
import com.palantir.conjure.python.poet.AliasSnippet;
import com.palantir.conjure.python.poet.BeanSnippet;
import com.palantir.conjure.python.poet.EnumSnippet;
import com.palantir.conjure.python.poet.EnumSnippet.PythonEnumValue;
import com.palantir.conjure.python.poet.PythonField;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonPackage;
import com.palantir.conjure.python.poet.PythonSnippet;
import com.palantir.conjure.python.poet.UnionSnippet;
import com.palantir.conjure.python.processors.packagename.PackageNameProcessor;
import com.palantir.conjure.python.processors.typename.TypeNameProcessor;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class PythonTypeGenerator {

    private final PackageNameProcessor implPackageNameProcessor;
    private final TypeNameProcessor implTypeNameProcessor;
    private final PackageNameProcessor definitionPackageNameProcessor;
    private final TypeNameProcessor definitionTypeNameProcessor;
    private final DealiasingTypeVisitor dealiasingTypeVisitor;
    private final PythonTypeNameVisitor pythonTypeNameVisitor;
    private final MyPyTypeNameVisitor myPyTypeNameVisitor;

    public PythonTypeGenerator(
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
        pythonTypeNameVisitor = new PythonTypeNameVisitor(implTypeNameProcessor);
        myPyTypeNameVisitor = new MyPyTypeNameVisitor(dealiasingTypeVisitor, implTypeNameProcessor);
    }

    public PythonSnippet generateType(TypeDefinition typeDef) {
        return typeDef.accept(new TypeDefinition.Visitor<PythonSnippet>() {
            @Override
            public PythonSnippet visitAlias(AliasDefinition value) {
                return generateAlias(value);
            }

            @Override
            public PythonSnippet visitEnum(EnumDefinition value) {
                return generateEnum(value);
            }

            @Override
            public PythonSnippet visitObject(ObjectDefinition value) {
                return generateBean(value);
            }

            @Override
            public PythonSnippet visitUnion(UnionDefinition value) {
                return generateUnion(value);
            }

            @Override
            public PythonSnippet visitUnknown(String unknownType) {
                throw new UnsupportedOperationException("cannot generate type for type def: " + unknownType);
            }
        });
    }

    private BeanSnippet generateBean(ObjectDefinition typeDef) {
        ImportTypeVisitor importVisitor =
                new ImportTypeVisitor(typeDef.getTypeName(), implTypeNameProcessor, implPackageNameProcessor);

        Set<PythonImport> imports = typeDef.getFields().stream()
                .flatMap(entry -> entry.getType().accept(importVisitor).stream())
                .collect(Collectors.toSet());

        List<PythonField> fields = typeDef.getFields().stream()
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

        return BeanSnippet.builder()
                .pythonPackage(PythonPackage.of(
                        implPackageNameProcessor.process(typeDef.getTypeName().getPackage())))
                .className(implTypeNameProcessor.process(typeDef.getTypeName()))
                .definitionPackage(PythonPackage.of(definitionPackageNameProcessor.process(
                        typeDef.getTypeName().getPackage())))
                .definitionName(definitionTypeNameProcessor.process(typeDef.getTypeName()))
                .addAllImports(BeanSnippet.DEFAULT_IMPORTS)
                .addAllImports(imports)
                .docs(typeDef.getDocs())
                .fields(fields)
                .build();
    }

    private EnumSnippet generateEnum(EnumDefinition typeDef) {
        return EnumSnippet.builder()
                .pythonPackage(PythonPackage.of(
                        implPackageNameProcessor.process(typeDef.getTypeName().getPackage())))
                .className(implTypeNameProcessor.process(typeDef.getTypeName()))
                .definitionPackage(PythonPackage.of(definitionPackageNameProcessor.process(
                        typeDef.getTypeName().getPackage())))
                .definitionName(definitionTypeNameProcessor.process(typeDef.getTypeName()))
                .addImports(EnumSnippet.CONJURE_IMPORT)
                .docs(typeDef.getDocs())
                .values(typeDef.getValues().stream()
                        .map(value -> PythonEnumValue.of(value.getValue(), value.getDocs()))
                        .collect(Collectors.toList()))
                .build();
    }

    private UnionSnippet generateUnion(UnionDefinition typeDef) {
        ImportTypeVisitor importVisitor =
                new ImportTypeVisitor(typeDef.getTypeName(), implTypeNameProcessor, implPackageNameProcessor);

        Set<PythonImport> imports = typeDef.getUnion().stream()
                .flatMap(fieldDefinition -> fieldDefinition.getType().accept(importVisitor).stream())
                .collect(Collectors.toSet());

        List<PythonField> options = typeDef.getUnion().stream()
                .map(unionMember -> {
                    Type conjureType = unionMember.getType();
                    return PythonField.builder()
                            .attributeName(CaseConverter.toCase(
                                    unionMember.getFieldName().get(), CaseConverter.Case.SNAKE_CASE))
                            .docs(unionMember.getDocs())
                            .jsonIdentifier(unionMember.getFieldName().get())
                            .myPyType(conjureType.accept(myPyTypeNameVisitor))
                            .pythonType(conjureType.accept(pythonTypeNameVisitor))
                            .isOptional(dealiasingTypeVisitor
                                    .dealias(unionMember.getType())
                                    .fold(_typeDefinition -> false, type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                            .build();
                })
                .collect(Collectors.toList());

        return UnionSnippet.builder()
                .pythonPackage(PythonPackage.of(
                        implPackageNameProcessor.process(typeDef.getTypeName().getPackage())))
                .className(implTypeNameProcessor.process(typeDef.getTypeName()))
                .definitionPackage(PythonPackage.of(definitionPackageNameProcessor.process(
                        typeDef.getTypeName().getPackage())))
                .definitionName(definitionTypeNameProcessor.process(typeDef.getTypeName()))
                .addAllImports(UnionSnippet.DEFAULT_IMPORTS)
                .addAllImports(imports)
                .docs(typeDef.getDocs())
                .options(options)
                .build();
    }

    private AliasSnippet generateAlias(AliasDefinition typeDef) {
        ImportTypeVisitor importVisitor =
                new ImportTypeVisitor(typeDef.getTypeName(), implTypeNameProcessor, implPackageNameProcessor);
        return AliasSnippet.builder()
                .pythonPackage(PythonPackage.of(
                        implPackageNameProcessor.process(typeDef.getTypeName().getPackage())))
                .className(implTypeNameProcessor.process(typeDef.getTypeName()))
                .aliasName(typeDef.getAlias().accept(pythonTypeNameVisitor))
                .aliasType(typeDef)
                .imports(ImmutableSet.copyOf(typeDef.getAlias().accept(importVisitor)))
                .build();
    }
}
