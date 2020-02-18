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

package com.palantir.conjure.python.poet;

import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.tokens.auth.ImmutablesStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutablesStyle
public interface AliasSnippet extends PythonSnippet {

    @Override
    @Value.Default
    default String idForSorting() {
        return className();
    }

    String className();

    String aliasName();

    AliasDefinition aliasType();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("%s = %s", className(), aliasName()));
        poetWriter.writeLine();
    }

    class Builder extends ImmutableAliasSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
