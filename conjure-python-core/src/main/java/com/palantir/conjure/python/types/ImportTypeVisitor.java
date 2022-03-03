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
import com.palantir.conjure.python.poet.NamedImport;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.processors.packagename.PackageNameProcessor;
import com.palantir.conjure.python.processors.typename.TypeNameProcessor;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public final class ImportTypeVisitor implements Type.Visitor<Set<PythonImport>> {
    public static final String CONJURE_PYTHON_CLIENT = "conjure_python_client";
    public static final String TYPING = "typing";

    private final TypeName typeName;
    private final TypeNameProcessor typeNameProcessor;
    private final PackageNameProcessor packageNameProcessor;

    public ImportTypeVisitor(
            TypeName typeName, TypeNameProcessor typeNameProcessor, PackageNameProcessor packageNameProcessor) {
        this.typeName = typeName;
        this.typeNameProcessor = typeNameProcessor;
        this.packageNameProcessor = packageNameProcessor;
    }

    @Override
    public Set<PythonImport> visitPrimitive(PrimitiveType value) {
        if (value.get() == PrimitiveType.Value.ANY) {
            return ImmutableSet.of(PythonImport.of(TYPING, NamedImport.of("Any")));
        } else if (value.get() == PrimitiveType.Value.BINARY) {
            return ImmutableSet.of(PythonImport.of(CONJURE_PYTHON_CLIENT, NamedImport.of("BinaryType")));
        }
        return ImmutableSet.of();
    }

    @Override
    public Set<PythonImport> visitOptional(OptionalType value) {
        return ImmutableSet.<PythonImport>builder()
                .add(PythonImport.of(TYPING, NamedImport.of("Optional")))
                .add(PythonImport.of(CONJURE_PYTHON_CLIENT, NamedImport.of("OptionalType")))
                .addAll(value.getItemType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitList(ListType value) {
        return ImmutableSet.<PythonImport>builder()
                .add(PythonImport.of(TYPING, NamedImport.of("List")))
                .add(PythonImport.of(CONJURE_PYTHON_CLIENT, NamedImport.of("ListType")))
                .addAll(value.getItemType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitSet(SetType value) {
        return ImmutableSet.<PythonImport>builder()
                .add(PythonImport.of(TYPING, NamedImport.of("Set")))
                .add(PythonImport.of(CONJURE_PYTHON_CLIENT, NamedImport.of("ListType")))
                .addAll(value.getItemType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitMap(MapType value) {
        return ImmutableSet.<PythonImport>builder()
                .add(PythonImport.of(TYPING, NamedImport.of("Dict")))
                .add(PythonImport.of(CONJURE_PYTHON_CLIENT, NamedImport.of("DictType")))
                .addAll(value.getKeyType().accept(this))
                .addAll(value.getValueType().accept(this))
                .build();
    }

    @Override
    public Set<PythonImport> visitReference(TypeName value) {
        if (typeName.getPackage().equals(value.getPackage())) {
            return ImmutableSet.of();
        }
        return ImmutableSet.of(PythonImport.of(
                relativePackage(
                        packageNameProcessor.process(typeName.getPackage()),
                        packageNameProcessor.process(value.getPackage())),
                NamedImport.of(typeNameProcessor.process(value))));
    }

    @Override
    public Set<PythonImport> visitExternal(ExternalReference value) {
        return value.getFallback().accept(this);
    }

    @Override
    public Set<PythonImport> visitUnknown(String unknownType) {
        throw new IllegalArgumentException("unknown type: " + unknownType);
    }

    static String relativePackage(String curPackage, String toPackage) {
        if (curPackage.equals(toPackage)) {
            return ".";
        }
        Path curPath = Paths.get(curPackage.replace(".", "/"));
        Path toPath = Paths.get(toPackage.replace(".", "/"));
        Path relativeImport = curPath.relativize(toPath);
        return relativeImport.toString().replace("../", "..");
    }
}
