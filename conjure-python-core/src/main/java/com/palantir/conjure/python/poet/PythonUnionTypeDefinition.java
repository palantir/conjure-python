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
import com.palantir.conjure.python.poet.PythonBean.PythonField;
import com.palantir.conjure.spec.Documentation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonUnionTypeDefinition extends PythonClass {

    ImmutableSet<PythonImport> DEFAULT_IMPORTS = ImmutableSet.of(
            PythonImport.of(PythonClassName.of("typing", "List")),
            PythonImport.of(PythonClassName.of("typing", "Set")),
            PythonImport.of(PythonClassName.of("typing", "Dict")),
            PythonImport.of(PythonClassName.of("typing", "Tuple")),
            PythonImport.of(PythonClassName.of("typing", "Optional")),
            PythonImport.of(PythonClassName.of("conjure_python_client", "*")));


    @Override
    @Value.Default
    default Set<PythonImport> requiredImports() {
        return DEFAULT_IMPORTS;
    }

    Optional<Documentation> docs();

    /**
     * The options in the union.
     */
    List<PythonField> options();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("class %s(ConjureUnionType):", className()));
        poetWriter.increaseIndent();
        docs().ifPresent(docs -> poetWriter.writeIndentedLine(String.format("\"\"\"%s\"\"\"", docs.get().trim())));

        poetWriter.writeLine();

        options().forEach(option -> poetWriter.writeIndentedLine("_%s = None # type: %s",
                option.attributeName(), option.myPyType()));

        poetWriter.writeLine();

        // record off the options
        poetWriter.writeIndentedLine("@classmethod");
        poetWriter.writeIndentedLine("def _options(cls):");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("# type: () -> Dict[str, ConjureFieldDefinition]"); // maybe....?
        poetWriter.writeIndentedLine("return {");
        poetWriter.increaseIndent();
        for (int i = 0; i < options().size(); i++) {
            PythonField option = options().get(i);
            poetWriter.writeIndentedLine("'%s': ConjureFieldDefinition('%s', %s)%s",
                    PythonIdentifierSanitizer.sanitize(option.attributeName()),
                    option.jsonIdentifier(),
                    option.pythonType(),
                    i == options().size() - 1 ? "" : ",");
        }
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine("}");
        poetWriter.decreaseIndent();

        poetWriter.writeLine();

        // constructor
        poetWriter.writeIndentedLine(String.format("def __init__(self, %s):",
                Joiner.on(", ").join(
                        options().stream()
                            .map(PythonField::attributeName)
                            .map(PythonIdentifierSanitizer::sanitize)
                            .map(attributeName -> String.format("%s=None", attributeName))
                            .collect(Collectors.toList()))));
        poetWriter.increaseIndent();
        // check we have exactly one non-null
        poetWriter.writeIndentedLine("if %s != 1:",
                Joiner.on(" + ").join(
                    options().stream()
                        .map(option -> String.format("(%s is not None)",
                                PythonIdentifierSanitizer.sanitize(option.attributeName())))
                        .collect(Collectors.toList())));
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("raise ValueError('a union must contain a single member')");
        poetWriter.decreaseIndent();
        // keep track of how many non-null there are
        poetWriter.writeLine();
        // save off
        options().forEach(option -> {
            poetWriter.writeIndentedLine("if %s is not None:",
                    PythonIdentifierSanitizer.sanitize(option.attributeName()));
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("self._%s = %s", option.attributeName(),
                    PythonIdentifierSanitizer.sanitize(option.attributeName()));
            poetWriter.writeIndentedLine("self._type = '%s'", option.jsonIdentifier());
            poetWriter.decreaseIndent();
        });
        poetWriter.decreaseIndent();

        // python @property for each member of the union
        options().forEach(option -> {
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("@property");
            poetWriter.writeIndentedLine(String.format("def %s(self):",
                    PythonIdentifierSanitizer.sanitize(option.attributeName())));

            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine(String.format("# type: () -> %s", option.myPyType()));
            option.docs().ifPresent(docs -> poetWriter.writeIndentedLine(String.format("\"\"\"%s\"\"\"",
                    docs.get().trim())));
            poetWriter.writeIndentedLine(String.format("return self._%s", option.attributeName()));
            poetWriter.decreaseIndent();
        });

        poetWriter.decreaseIndent();
        poetWriter.writeLine();
    }

    class Builder extends ImmutablePythonUnionTypeDefinition.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
