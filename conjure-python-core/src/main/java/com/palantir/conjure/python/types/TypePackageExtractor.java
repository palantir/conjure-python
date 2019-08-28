/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.UnionDefinition;

public enum TypePackageExtractor implements TypeDefinition.Visitor<String> {
    INSTANCE;

    @Override
    public String visitAlias(AliasDefinition value) {
        return value.getTypeName().getPackage();
    }

    @Override
    public String visitEnum(EnumDefinition value) {
        return value.getTypeName().getPackage();
    }

    @Override
    public String visitObject(ObjectDefinition value) {
        return value.getTypeName().getPackage();
    }

    @Override
    public String visitUnion(UnionDefinition value) {
        return value.getTypeName().getPackage();
    }

    @Override
    public String visitUnknown(String unknownType) {
        throw new IllegalStateException("Unsupported type: " + unknownType);
    }
}
