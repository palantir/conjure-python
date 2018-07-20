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

import com.google.common.collect.Streams;
import java.util.List;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonFile extends Emittable {

    @Value.Default
    default String packageName() {
        return "";
    }

    String fileName();

    Set<PythonImport> imports();

    List<PythonClass> contents();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            Streams.concat(imports().stream(), contents().stream()
                    .flatMap(pythonClass -> pythonClass.requiredImports().stream()))
                    .distinct()
                    .sorted()
                    .forEach(poetWriter::emit);

            poetWriter.writeLine();
            contents().stream().forEach(poetWriter::emit);
        });
    }

    class Builder extends ImmutablePythonFile.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
