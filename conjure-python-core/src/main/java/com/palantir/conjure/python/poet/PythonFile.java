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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonFile extends Emittable {

    String packageName();

    String fileName();

    List<PythonSnippet> contents();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            Map<String, Set<String>> imports = contents().stream()
                    .flatMap(pythonSnippet -> pythonSnippet.imports().stream())
                    .collect(Collectors.toMap(
                            PythonImport::moduleSpecifier,
                            PythonImport::namedImports,
                            (strings, strings2) -> Stream.concat(strings.stream(), strings2.stream())
                                    .collect(Collectors.toSet())));

            imports.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach((entry) -> PythonImport.builder()
                    .moduleSpecifier(entry.getKey())
                    .namedImports(entry.getValue())
                    .build()
                    .emit(poetWriter));
            if (imports.size() > 0) {
                poetWriter.writeLine();
            }

            contents().stream().sorted(new PythonFileComparator()).forEach(poetWriter::emit);
        });
    }

    class Builder extends ImmutablePythonFile.Builder {}

    static Builder builder() {
        return new Builder();
    }

    class PythonFileComparator implements Comparator<PythonSnippet> {
        @Override
        public int compare(PythonSnippet pc1, PythonSnippet pc2) {
            // PythonAliases need to occur last, since they potentially reference
            // objects defined in the current module
            if (pc1 instanceof AliasSnippet && !(pc2 instanceof AliasSnippet)) {
                return 1;
            } else if (!(pc1 instanceof AliasSnippet) && pc2 instanceof AliasSnippet) {
                return -1;
            } else {
                return Comparator.comparing(PythonSnippet::name).compare(pc1, pc2);
            }
        }
    }
}
