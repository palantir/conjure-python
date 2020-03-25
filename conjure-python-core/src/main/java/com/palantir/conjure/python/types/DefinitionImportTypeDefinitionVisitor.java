/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.python.poet.NamedImport;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.processors.typename.TypeNameProcessor;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.spec.UnionDefinition;
import java.util.List;

public final class DefinitionImportTypeDefinitionVisitor implements TypeDefinition.Visitor<List<PythonImport>> {

    private final String moduleSpecifier;
    private final TypeNameProcessor implTypeNameProcessor;
    private final TypeNameProcessor definitionTypeNameProcessor;

    public DefinitionImportTypeDefinitionVisitor(
            String moduleSpecifier,
            TypeNameProcessor implTypeNameProcessor,
            TypeNameProcessor definitionTypeNameProcessor) {
        this.moduleSpecifier = moduleSpecifier;
        this.implTypeNameProcessor = implTypeNameProcessor;
        this.definitionTypeNameProcessor = definitionTypeNameProcessor;
    }

    @Override
    public List<PythonImport> visitAlias(AliasDefinition value) {
        return ImmutableList.of(getTypeImport(value.getTypeName()));
    }

    @Override
    public List<PythonImport> visitEnum(EnumDefinition value) {
        return ImmutableList.of(getTypeImport(value.getTypeName()));
    }

    @Override
    public List<PythonImport> visitObject(ObjectDefinition value) {
        return ImmutableList.of(getTypeImport(value.getTypeName()));
    }

    @Override
    public List<PythonImport> visitUnion(UnionDefinition value) {
        PythonImport unionImport = PythonImport.of(
                moduleSpecifier,
                NamedImport.of(
                        String.format("%sVisitor", implTypeNameProcessor.process(value.getTypeName())),
                        String.format("%sVisitor", definitionTypeNameProcessor.process(value.getTypeName()))));
        return ImmutableList.of(getTypeImport(value.getTypeName()), unionImport);
    }

    @Override
    public List<PythonImport> visitUnknown(String unknownType) {
        throw new IllegalArgumentException("unknown type definition: " + unknownType);
    }

    private PythonImport getTypeImport(TypeName typeName) {
        return PythonImport.of(
                moduleSpecifier,
                NamedImport.of(implTypeNameProcessor.process(typeName), definitionTypeNameProcessor.process(typeName)));
    }
}
