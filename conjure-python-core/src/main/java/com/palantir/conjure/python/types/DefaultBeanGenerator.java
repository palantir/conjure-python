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

import com.palantir.conjure.python.poet.AliasSnippet;
import com.palantir.conjure.python.poet.BeanSnippet;
import com.palantir.conjure.python.poet.EnumSnippet;
import com.palantir.conjure.python.poet.EnumSnippet.PythonEnumValue;
import com.palantir.conjure.python.poet.PythonField;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonSnippet;
import com.palantir.conjure.python.poet.UnionSnippet;
import com.palantir.conjure.python.util.CaseConverter;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DefaultBeanGenerator implements PythonBeanGenerator {

    @Override
    public PythonSnippet generateType(
            TypeDefinition typeDef,
            Function<TypeName, ImportTypeVisitor> importTypeVisitorFactory,
            DealiasingTypeVisitor dealiasingTypeVisitor,
            TypeMapper mapper,
            TypeMapper myPyMapper) {
        if (typeDef.accept(TypeDefinitionVisitor.IS_OBJECT)) {
            return generateBean(
                    typeDef.accept(TypeDefinitionVisitor.OBJECT),
                    importTypeVisitorFactory,
                    dealiasingTypeVisitor,
                    mapper,
                    myPyMapper);
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ENUM)) {
            return generateEnum(typeDef.accept(TypeDefinitionVisitor.ENUM));
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_UNION)) {
            return generateUnion(
                    typeDef.accept(TypeDefinitionVisitor.UNION),
                    importTypeVisitorFactory,
                    dealiasingTypeVisitor,
                    mapper,
                    myPyMapper);
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            return generateAlias(typeDef.accept(TypeDefinitionVisitor.ALIAS), importTypeVisitorFactory, mapper);
        } else {
            throw new UnsupportedOperationException("cannot generate type for type def: " + typeDef);
        }
    }

    private BeanSnippet generateBean(
            ObjectDefinition typeDef,
            Function<TypeName, ImportTypeVisitor> importTypeVisitorFactory,
            DealiasingTypeVisitor dealiasingTypeVisitor,
            TypeMapper mapper,
            TypeMapper myPyMapper) {
        ImportTypeVisitor importVisitor = importTypeVisitorFactory.apply(typeDef.getTypeName());

        Set<PythonImport> imports = typeDef.getFields()
                .stream()
                .flatMap(entry -> entry.getType().accept(importVisitor).stream())
                .collect(Collectors.toSet());

        List<PythonField> fields = typeDef.getFields()
                .stream()
                .map(entry -> PythonField.builder()
                        .attributeName(CaseConverter.toCase(
                                entry.getFieldName().get(), CaseConverter.Case.SNAKE_CASE))
                        .jsonIdentifier(entry.getFieldName().get())
                        .docs(entry.getDocs())
                        .pythonType(mapper.getTypeName(entry.getType()))
                        .myPyType(myPyMapper.getTypeName(entry.getType()))
                        .isOptional(dealiasingTypeVisitor.dealias(entry.getType()).fold(
                                typeDefinition -> false,
                                type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                        .build())
                .collect(Collectors.toList());

        return BeanSnippet.builder()
                .name(typeDef.getTypeName().getName())
                .addImports(BeanSnippet.CONJURE_IMPORT)
                .addAllImports(imports)
                .docs(typeDef.getDocs())
                .fields(fields)
                .build();
    }

    private EnumSnippet generateEnum(EnumDefinition typeDef) {
        return EnumSnippet.builder()
                .name(typeDef.getTypeName().getName())
                .addImports(EnumSnippet.CONJURE_IMPORT)
                .docs(typeDef.getDocs())
                .values(typeDef.getValues().stream()
                        .map(value -> PythonEnumValue.of(value.getValue(), value.getDocs()))
                        .collect(Collectors.toList()))
                .build();
    }

    private UnionSnippet generateUnion(
            UnionDefinition typeDef,
            Function<TypeName, ImportTypeVisitor> importTypeVisitorFactory,
            DealiasingTypeVisitor dealiasingTypeVisitor,
            TypeMapper mapper,
            TypeMapper myPyMapper) {
        ImportTypeVisitor importVisitor = importTypeVisitorFactory.apply(typeDef.getTypeName());

        Set<PythonImport> imports = typeDef.getUnion()
                .stream()
                .flatMap(fieldDefinition -> fieldDefinition.getType().accept(importVisitor).stream())
                .collect(Collectors.toSet());

        List<PythonField> options = typeDef.getUnion()
                .stream()
                .map(unionMember -> {
                    Type conjureType = unionMember.getType();
                    return PythonField.builder()
                            .attributeName(CaseConverter.toCase(
                                    unionMember.getFieldName().get(), CaseConverter.Case.SNAKE_CASE))
                            .docs(unionMember.getDocs())
                            .jsonIdentifier(unionMember.getFieldName().get())
                            .myPyType(myPyMapper.getTypeName(conjureType))
                            .pythonType(mapper.getTypeName(conjureType))
                            .isOptional(dealiasingTypeVisitor.dealias(unionMember.getType()).fold(
                                    typeDefinition -> false,
                                    type -> type.accept(TypeVisitor.IS_OPTIONAL)))
                            .build();
                })
                .collect(Collectors.toList());

        return UnionSnippet.builder()
                .name(typeDef.getTypeName().getName())
                .addImports(UnionSnippet.CONJURE_IMPORT)
                .addAllImports(imports)
                .docs(typeDef.getDocs())
                .options(options)
                .build();
    }

    private AliasSnippet generateAlias(
            AliasDefinition typeDef,
            Function<TypeName, ImportTypeVisitor> importTypeVisitorFactory,
            TypeMapper mapper) {
        ImportTypeVisitor importVisitor = importTypeVisitorFactory.apply(typeDef.getTypeName());
        return AliasSnippet.builder()
                .name(typeDef.getTypeName().getName())
                .aliasName(mapper.getTypeName(typeDef.getAlias()))
                .imports(new HashSet<>(typeDef.getAlias().accept(importVisitor)))
                .build();
    }

}
