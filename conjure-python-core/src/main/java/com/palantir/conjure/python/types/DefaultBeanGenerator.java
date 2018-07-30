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
import com.palantir.conjure.python.PackageNameProcessor;
import com.palantir.conjure.python.poet.PythonAlias;
import com.palantir.conjure.python.poet.PythonBean;
import com.palantir.conjure.python.poet.PythonBean.PythonField;
import com.palantir.conjure.python.poet.PythonClass;
import com.palantir.conjure.python.poet.PythonEnum;
import com.palantir.conjure.python.poet.PythonEnum.PythonEnumValue;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonUnionTypeDefinition;
import com.palantir.conjure.python.util.CaseConverter;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import com.palantir.conjure.visitor.TypeVisitor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class DefaultBeanGenerator implements PythonBeanGenerator {

    // TODO(qchen): remove?
    private final Set<ExperimentalFeatures> enabledExperimentalFeatures;

    public DefaultBeanGenerator(Set<ExperimentalFeatures> enabledExperimentalFeatures) {
        this.enabledExperimentalFeatures = ImmutableSet.copyOf(enabledExperimentalFeatures);
    }

    @Override
    public PythonClass generateObject(List<TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            TypeDefinition typeDef) {
        if (typeDef.accept(TypeDefinitionVisitor.IS_OBJECT)) {
            return generateObject(types, packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.OBJECT));
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ENUM)) {
            return generateObject(packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.ENUM));
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_UNION)) {
            return generateObject(types, packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.UNION));
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            return generateObject(types, packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.ALIAS));
        } else {
            throw new UnsupportedOperationException("cannot generate type for type def: " + typeDef);
        }
    }

    private PythonClass generateObject(
            List<TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            UnionDefinition typeDef) {

        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types));
        TypeMapper myPyMapper = new TypeMapper(new MyPyTypeNameVisitor(types));

        ReferencedTypeNameVisitor referencedTypeNameVisitor = new ReferencedTypeNameVisitor(
                types, packageNameProcessor);

        String packageName = packageNameProcessor.getPackageName(typeDef.getTypeName().getPackage());

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
                            .isOptional(unionMember.getType().accept(TypeVisitor.IS_OPTIONAL))
                            .build();
                })
                .collect(Collectors.toList());

        Set<PythonImport> imports = typeDef.getUnion()
                .stream()
                .flatMap(entry -> entry.getType().accept(referencedTypeNameVisitor).stream())
                .filter(entry -> !entry.conjurePackage().equals(packageName)) // don't need to import if in this file
                .map(referencedClassName -> PythonImport.of(referencedClassName, packageName))
                .collect(Collectors.toSet());

        return PythonUnionTypeDefinition.builder()
                .packageName(packageName)
                .className(typeDef.getTypeName().getName())
                .docs(typeDef.getDocs())
                .addAllOptions(options)
                .addAllRequiredImports(imports)
                .build();
    }

    private PythonEnum generateObject(PackageNameProcessor packageNameProcessor, EnumDefinition typeDef) {

        String packageName = packageNameProcessor.getPackageName(typeDef.getTypeName().getPackage());

        return PythonEnum.builder()
                .packageName(packageName)
                .className(typeDef.getTypeName().getName())
                .docs(typeDef.getDocs())
                .values(typeDef.getValues().stream()
                        .map(value -> PythonEnumValue.of(value.getValue(), value.getDocs()))
                        .collect(Collectors.toList()))
                .build();
    }

    private PythonBean generateObject(
            List<TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            ObjectDefinition typeDef) {

        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types));
        TypeMapper myPyMapper = new TypeMapper(new MyPyTypeNameVisitor(types));
        ReferencedTypeNameVisitor referencedTypeNameVisitor = new ReferencedTypeNameVisitor(
                types, packageNameProcessor);

        String packageName = packageNameProcessor.getPackageName(typeDef.getTypeName().getPackage());

        Set<PythonImport> imports = typeDef.getFields()
                .stream()
                .flatMap(entry -> entry.getType().accept(referencedTypeNameVisitor).stream())
                .filter(entry -> !entry.conjurePackage().equals(packageName)) // don't need to import if in this file
                .map(referencedClassName -> PythonImport.of(referencedClassName, packageName))
                .collect(Collectors.toSet());

        return PythonBean.builder()
                .packageName(packageName)
                .addAllRequiredImports(PythonBean.DEFAULT_IMPORTS)
                .addAllRequiredImports(imports)
                .className(typeDef.getTypeName().getName())
                .docs(typeDef.getDocs())
                .fields(typeDef.getFields()
                        .stream()
                        .map(entry -> PythonField.builder()
                                .attributeName(CaseConverter.toCase(
                                        entry.getFieldName().get(), CaseConverter.Case.SNAKE_CASE))
                                .jsonIdentifier(entry.getFieldName().get())
                                .docs(entry.getDocs())
                                .pythonType(mapper.getTypeName(entry.getType()))
                                .myPyType(myPyMapper.getTypeName(entry.getType()))
                                .isOptional(entry.getType().accept(TypeVisitor.IS_OPTIONAL))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private PythonAlias generateObject(
            List<TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            AliasDefinition typeDef) {
        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types));
        ReferencedTypeNameVisitor referencedTypeNameVisitor = new ReferencedTypeNameVisitor(
                types, packageNameProcessor);
        String packageName = packageNameProcessor.getPackageName(typeDef.getTypeName().getPackage());

        Set<PythonImport> imports = typeDef.getAlias().accept(referencedTypeNameVisitor)
                .stream()
                .filter(entry -> !entry.conjurePackage().equals(packageName)) // don't need to import if in this file
                .map(referencedClassName -> PythonImport.of(referencedClassName, packageName))
                .collect(Collectors.toSet());

        return PythonAlias.builder()
                .className(typeDef.getTypeName().getName())
                .aliasTarget(mapper.getTypeName(typeDef.getAlias()))
                .packageName(packageName)
                .addAllRequiredImports(imports)
                .build();
    }

}
