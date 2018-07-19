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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.spec.Documentation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonEnum extends PythonClass {

    ImmutableSet<PythonImport> DEFAULT_IMPORTS = ImmutableSet.of(
            PythonImport.of(PythonClassName.of("conjure_python_client", "ConjureEnumType")));

    @Override
    @Value.Default
    default Set<PythonImport> requiredImports() {
        return DEFAULT_IMPORTS;
    }

    List<PythonEnumValue> values();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            poetWriter.writeIndentedLine(String.format("class %s(ConjureEnumType):", className()));
            poetWriter.increaseIndent();
            docs().ifPresent(docs -> poetWriter.writeIndentedLine(String.format("\"\"\"%s\"\"\"", docs.get().trim())));

            poetWriter.writeLine();

            List<PythonEnumValue> allValues = ImmutableList.<PythonEnumValue>builder()
                    .addAll(values())
                    .add(PythonEnumValue.of("UNKNOWN", Optional.empty()))
                    .build();

            allValues.forEach(value -> {
                poetWriter.writeIndentedLine("%s = '%s'", value.name(), value.name());
                poetWriter.writeIndentedLine("'''%s'''", value.name());
            });

            poetWriter.writeLine();
            poetWriter.writeIndentedLine("def __reduce_ex__(self, proto):");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("return self.__class__, (self.name,)");
            poetWriter.decreaseIndent();

            poetWriter.decreaseIndent();
            poetWriter.writeLine();
        });
    }

    class Builder extends ImmutablePythonEnum.Builder {}

    static Builder builder() {
        return new Builder();
    }

    @Value.Immutable
    public interface PythonEnumValue {

        String name();

        Optional<Documentation> docs();

        static PythonEnumValue of(String name, Optional<Documentation> docs) {
            return ImmutablePythonEnumValue.builder().name(name).docs(docs).build();
        }

    }
}
