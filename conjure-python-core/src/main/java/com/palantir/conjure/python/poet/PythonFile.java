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

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonFile extends Emittable {

    @Value.Default
    default String packageName() {
        return "";
    }

    @Value.Default
    default String fileName() {
        return "__init__.py";
    }

    Set<PythonImport> imports();

    List<PythonClass> contents();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            if (packageName().length() > 0) {
                poetWriter.writeLine(String.format("# this is package %s", packageName()));
            }
            imports().stream().sorted().forEach(poetWriter::emit);
            poetWriter.writeLine();

            contents().stream().sorted(new PythonClassSerializationComparator()).forEach(poetWriter::emit);
        });
    }

    class Builder extends ImmutablePythonFile.Builder {}

    static Builder builder() {
        return new Builder();
    }

    class PythonClassSerializationComparator implements Comparator<PythonClass> {
        @Override
        public int compare(PythonClass pc1, PythonClass pc2) {
            // PythonAliases need to occur last, since they potentially reference
            // objects defined in the current module
            if (pc1 instanceof PythonAlias && !(pc2 instanceof PythonAlias)) {
                return 1;
            } else if (!(pc1 instanceof PythonAlias) && pc2 instanceof PythonAlias) {
                return -1;
            } else {
                return Comparator.comparing(PythonClass::className).compare(pc1, pc2);
            }
        }
    }
}
