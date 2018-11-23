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

import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonImport extends Emittable {
    String moduleSpecifier();

    Set<String> namedImports();

    static PythonImport of(String moduleSpecifier, String namedImport) {
        return builder()
                .moduleSpecifier(moduleSpecifier)
                .addNamedImports(namedImport)
                .build();
    }

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("from %s import %s",
                moduleSpecifier(), String.join(", ", namedImports())));
    }

    class Builder extends ImmutablePythonImport.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
