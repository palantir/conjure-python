/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.python.PackageNameProcessor;
import com.palantir.conjure.python.poet.PythonClassName;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class ImportsVisitor implements Type.Visitor<Set<PythonImport>> {
    private TypeName currentType;
    private PackageNameProcessor packageNameProcessor;
    private Map<TypeName, TypeDefinition> knownTypes;

    public ImportsVisitor(
            TypeName currentType,
            PackageNameProcessor packageNameProcessor,
            Map<TypeName, TypeDefinition> knownTypes) {
        this.currentType = currentType;
        this.packageNameProcessor = packageNameProcessor;
        this.knownTypes = knownTypes;
    }

    @Override
    public Set<PythonImport> visitPrimitive(PrimitiveType value) {
        if (value.equals(PrimitiveType.ANY)) {
            return ImmutableSet.of(PythonImport.of(PythonClassName.of("typing", "Any")));
        } else if (value.equals(PrimitiveType.BINARY)) {
            return ImmutableSet.of(PythonImport.of(PythonClassName.of("conjure_python_client", "BinaryType")));
        }
        return Collections.emptySet();
    }

    @Override
    public Set<PythonImport> visitOptional(OptionalType value) {
        ImmutableSet.Builder<PythonImport> setBuilder = ImmutableSet.builder();
        return setBuilder
                .add(PythonImport.of(PythonClassName.of("typing", "Optional")))
                .add(PythonImport.of(PythonClassName.of("conjure_python_client", "OptionalType")))
                .addAll(value.getItemType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitList(ListType value) {
        ImmutableSet.Builder<PythonImport> setBuilder = ImmutableSet.builder();
        return setBuilder
                .add(PythonImport.of(PythonClassName.of("typing", "List")))
                .add(PythonImport.of(PythonClassName.of("conjure_python_client", "ListType")))
                .addAll(value.getItemType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitSet(SetType value) {
        ImmutableSet.Builder<PythonImport> setBuilder = ImmutableSet.builder();
        return setBuilder
                .add(PythonImport.of(PythonClassName.of("typing", "Set")))
                .add(PythonImport.of(PythonClassName.of("conjure_python_client", "ListType")))
                .addAll(value.getItemType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitMap(MapType value) {
        ImmutableSet.Builder<PythonImport> setBuilder = ImmutableSet.builder();
        return setBuilder
                .add(PythonImport.of(PythonClassName.of("typing", "Dict")))
                .add(PythonImport.of(PythonClassName.of("conjure_python_client", "DictType")))
                .addAll(value.getKeyType().accept(this))
                .addAll(value.getValueType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitReference(TypeName value) {
        Preconditions.checkState(knownTypes.containsKey(value), "Unknown TypeName %s", value);
        return ImmutableSet.of(PythonImport.of(
                PythonClassName.of(getRelativePath(value), value.getName())));
    }

    @Override
    public Set<PythonImport> visitExternal(ExternalReference value) {
        // TODO(forozco): handle python external references
        return Collections.emptySet();
    }

    @Override
    public Set<PythonImport> visitUnknown(String unknownType) {
        return Collections.emptySet();
    }

    private String getRelativePath(TypeName targetType) {
        if (targetType.getPackage().equals(currentType.getPackage())) {
            return String.format(".%s", targetType.getName());
        } else {
            String targetPackageName = packageNameProcessor.getPackageName(targetType.getPackage()).split("\\.")[1];
            return String.format("..%s.%s", targetPackageName, targetType.getName());
        }
    }
}
