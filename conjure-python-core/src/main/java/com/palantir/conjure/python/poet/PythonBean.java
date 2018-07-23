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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.spec.Documentation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonBean extends PythonClass {

    ImmutableSet<PythonImport> DEFAULT_IMPORTS = ImmutableSet.of(
            PythonImport.of(PythonClassName.of("typing", "List")),
            PythonImport.of(PythonClassName.of("typing", "Set")),
            PythonImport.of(PythonClassName.of("typing", "Dict")),
            PythonImport.of(PythonClassName.of("typing", "Tuple")),
            PythonImport.of(PythonClassName.of("typing", "Optional")),
            PythonImport.of(PythonClassName.of("conjure_python_client", "ConjureBeanType")),
            PythonImport.of(PythonClassName.of("conjure_python_client", "ConjureFieldDefinition")));

    @Override
    @Value.Default
    default Set<PythonImport> requiredImports() {
        return DEFAULT_IMPORTS;
    }

    Optional<Documentation> docs();

    List<PythonField> fields();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("class %s(ConjureBeanType):", className()));
        poetWriter.increaseIndent();
        docs().ifPresent(docs -> poetWriter.writeIndentedLine(String.format("\"\"\"%s\"\"\"", docs.get().trim())));

        poetWriter.writeLine();

        // record off the fields, for things like serialization (python... has no types)
        poetWriter.writeIndentedLine("@classmethod");
        poetWriter.writeIndentedLine("def _fields(cls):");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("# type: () -> Dict[str, ConjureFieldDefinition]");
        poetWriter.writeIndentedLine("return {");
        poetWriter.increaseIndent();
        for (int i = 0; i < fields().size(); i++) {
            PythonField field = fields().get(i);
            poetWriter.writeIndentedLine(String.format("'%s': ConjureFieldDefinition('%s', %s)%s",
                    PythonIdentifierSanitizer.sanitize(field.attributeName()),
                    field.jsonIdentifier(),
                    field.pythonType(),
                    i == fields().size() - 1 ? "" : ","));
        }
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine("}");
        poetWriter.decreaseIndent();

        poetWriter.writeLine();

        // entry for each field
        fields().forEach(field -> {
            poetWriter.writeIndentedLine(String.format("_%s = None # type: %s",
                    field.attributeName(),
                    field.myPyType()));
        });

        poetWriter.writeLine();

        // constructor -- only if there are fields
        if (!fields().isEmpty()) {
            poetWriter.writeIndentedLine(String.format("def __init__(self, %s):",
                    Joiner.on(", ").join(
                            fields().stream().map(PythonField::attributeName)
                                    .map(PythonIdentifierSanitizer::sanitize).collect(Collectors.toList()))));
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine(String.format("# type: (%s) -> None",
                    Joiner.on(", ").join(fields().stream().map(PythonField::myPyType).collect(Collectors.toList()))));
            fields().forEach(field -> {
                poetWriter.writeIndentedLine(
                        String.format("self._%s = %s", field.attributeName(),
                                PythonIdentifierSanitizer.sanitize(field.attributeName())));
            });
            poetWriter.decreaseIndent();
        }

        // each property
        fields().forEach(field -> {
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("@property");
            poetWriter.writeIndentedLine(String.format("def %s(self):",
                    PythonIdentifierSanitizer.sanitize(field.attributeName())));

            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine(String.format("# type: () -> %s", field.myPyType()));
            field.docs().ifPresent(docs -> poetWriter.writeIndentedLine(String.format("\"\"\"%s\"\"\"",
                    docs.get().trim())));
            poetWriter.writeIndentedLine(String.format("return self._%s", field.attributeName()));
            poetWriter.decreaseIndent();
        });

        // end of class def
        poetWriter.decreaseIndent();
        poetWriter.writeLine();

    }

    class Builder extends ImmutablePythonBean.Builder {}

    static Builder builder() {
        return new Builder();
    }

    @Value.Immutable
    public interface PythonField {

        String attributeName();

        String jsonIdentifier();

        /**
         * The python type (or a conjure fake type) for this type.
         */
        String pythonType();

        /**
         * The mypy type for this type.
         */
        String myPyType();

        Optional<Documentation> docs();

        class Builder extends ImmutablePythonField.Builder {}

        static Builder builder() {
            return new Builder();
        }

    }

}
