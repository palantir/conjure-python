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

import com.google.common.collect.Iterables;
import com.palantir.conjure.python.PackageNameProcessor;
import com.palantir.conjure.python.PythonFileGenerator;
import com.palantir.conjure.python.poet.PythonAlias;
import com.palantir.conjure.python.poet.PythonBean;
import com.palantir.conjure.python.poet.PythonBean.PythonField;
import com.palantir.conjure.python.poet.PythonEnum;
import com.palantir.conjure.python.poet.PythonEnum.PythonEnumValue;
import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonUnionTypeDefinition;
import com.palantir.conjure.python.util.CaseConverter;
import com.palantir.conjure.python.util.ImportsVisitor;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class DefaultBeanGeneratorPython implements PythonFileGenerator<TypeDefinition> {

    @Override
    public PythonFile generateFile(
            Map<TypeName, TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            TypeDefinition typeDef) {
        if (typeDef.accept(TypeDefinitionVisitor.IS_OBJECT)) {
            return generateObject(types, packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.OBJECT));
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ENUM)) {
            return generateEnum(packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.ENUM));
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_UNION)) {
            return generateUnion(types, packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.UNION));
        } else if (typeDef.accept(TypeDefinitionVisitor.IS_ALIAS)) {
            return generateAlias(types, packageNameProcessor, typeDef.accept(TypeDefinitionVisitor.ALIAS));
        } else {
            throw new UnsupportedOperationException("cannot generate type for type def: " + typeDef);
        }
    }

    private boolean isRelativeImport(PythonImport imp) {
        return imp.className().conjurePackage().startsWith(".");
    }

    private PythonFile generateObject(
            Map<TypeName, TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            ObjectDefinition typeDef) {

        ImportsVisitor importsVisitor = new ImportsVisitor(typeDef.getTypeName(), packageNameProcessor, types);
        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types.keySet()));
        TypeMapper myPyMapper = new TypeMapper(new MyPyTypeNameVisitor(types.keySet()));
        String packageName = packageNameProcessor.getPackageName(typeDef.getTypeName().getPackage());

        Set<PythonImport> imports = typeDef.getFields()
                .stream()
                .flatMap(entry -> entry.getType().accept(importsVisitor).stream())
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
                        .build())
                .collect(Collectors.toList());

        return PythonFile.builder()
                .fileName(String.format("%s.py", typeDef.getTypeName().getName()))
                .imports(Iterables.filter(imports, imp -> !isRelativeImport(imp)))
                .bottomImports(Iterables.filter(imports, this::isRelativeImport))
                .packageName(packageName)
                .addContents(PythonBean.builder()
                        .className(typeDef.getTypeName().getName())
                        .docs(typeDef.getDocs())
                        .fields(fields)
                        .build())
                .build();
    }

    private PythonFile generateUnion(
            Map<TypeName, TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            UnionDefinition typeDef) {

        ImportsVisitor importsVisitor = new ImportsVisitor(typeDef.getTypeName(), packageNameProcessor, types);
        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types.keySet()));
        TypeMapper myPyMapper = new TypeMapper(new MyPyTypeNameVisitor(types.keySet()));
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
                            .build();
                })
                .collect(Collectors.toList());

        Set<PythonImport> imports = typeDef.getUnion()
                .stream()
                .flatMap(entry -> entry.getType().accept(importsVisitor).stream())
                .collect(Collectors.toSet());

        return PythonFile.builder()
                .fileName(String.format("%s.py", typeDef.getTypeName().getName()))
                .imports(Iterables.filter(imports, imp -> !isRelativeImport(imp)))
                .bottomImports(Iterables.filter(imports, this::isRelativeImport))
                .packageName(packageName)
                .addContents(
                        PythonUnionTypeDefinition.builder()
                                .className(typeDef.getTypeName().getName())
                                .docs(typeDef.getDocs())
                                .addAllOptions(options)
                                .build())
                .build();
    }

    private PythonFile generateEnum(PackageNameProcessor packageNameProcessor, EnumDefinition typeDef) {

        String packageName = packageNameProcessor.getPackageName(typeDef.getTypeName().getPackage());

        return PythonFile.builder()
                .fileName(String.format("%s.py", typeDef.getTypeName().getName()))
                .packageName(packageName)
                .addContents(PythonEnum.builder()
                        .className(typeDef.getTypeName().getName())
                        .docs(typeDef.getDocs())
                        .values(typeDef.getValues().stream()
                                .map(value -> PythonEnumValue.of(value.getValue(), value.getDocs()))
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }

    private PythonFile generateAlias(
            Map<TypeName, TypeDefinition> types,
            PackageNameProcessor packageNameProcessor,
            AliasDefinition typeDef) {

        ImportsVisitor importsVisitor = new ImportsVisitor(typeDef.getTypeName(), packageNameProcessor, types);
        TypeMapper mapper = new TypeMapper(new DefaultTypeNameVisitor(types.keySet()));
        String packageName = packageNameProcessor.getPackageName(typeDef.getTypeName().getPackage());

        return PythonFile.builder()
                .fileName(String.format("%s.py", typeDef.getTypeName().getName()))
                .imports(typeDef.getAlias().accept(importsVisitor))
                .packageName(packageName)
                .addContents(PythonAlias.builder()
                        .className(typeDef.getTypeName().getName())
                        .aliasTarget(mapper.getTypeName(typeDef.getAlias()))
                        .build())
                .build();
    }

}
