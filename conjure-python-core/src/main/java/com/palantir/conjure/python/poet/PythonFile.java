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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonFile extends Emittable {

    @Value.Default
    default String packageName() {
        return "";
    }

    String fileName();

    Set<PythonImport> imports();

    /**
     * Imports that should be put at the bottom, to avoid circular references.
     * Note: these MUST be _module attribute_ imports as in {@code from <module> import <attribute>},
     * using {@code from <package> import <module>} will not work.
     * <p>
     * See: <a href="https://gist.github.com/datagrok/40bf84d5870c41a77dc6">Python circular imports edge cases</a>
     */
    Set<PythonImport> bottomImports();

    List<PythonClass> contents();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            Stream
                    .concat(imports().stream(),
                            contents().stream().map(PythonClass::requiredImports).flatMap(Collection::stream))
                    .distinct()
                    .sorted()
                    .forEach(poetWriter::emit);

            poetWriter.writeLine();
            contents().forEach(poetWriter::emit);

            bottomImports().forEach(poetWriter::emit);
        });
    }

    class Builder extends ImmutablePythonFile.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
