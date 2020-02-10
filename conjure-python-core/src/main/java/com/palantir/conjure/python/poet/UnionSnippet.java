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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.palantir.conjure.python.types.ImportTypeVisitor;
import com.palantir.conjure.spec.Documentation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.immutables.value.Value;

@Value.Immutable
public interface UnionSnippet extends PythonSnippet {
    ImmutableList<PythonImport> DEFAULT_IMPORTS = ImmutableList.of(
            PythonImport.builder()
                    .moduleSpecifier(ImportTypeVisitor.CONJURE_PYTHON_CLIENT)
                    .addNamedImports("ConjureUnionType", "ConjureFieldDefinition")
                    .build(),
            PythonImport.builder()
                    .moduleSpecifier("abc")
                    .addNamedImports("ABCMeta", "abstractmethod")
                    .build(),
            PythonImport.builder()
                    .moduleSpecifier(ImportTypeVisitor.TYPING)
                    .addNamedImports("Dict", "Any")
                    .build(),
            PythonImport.of("builtins"));
    ImmutableSet<String> PROTECTED_FIELDS = ImmutableSet.of("options");

    @Override
    @Value.Default
    default String idForSorting() {
        return className();
    }

    String className();

    Optional<Documentation> docs();

    List<PythonField> options();

    /** The name of the option as a constructor / method parameter. */
    static String parameterName(PythonField option) {
        return PythonIdentifierSanitizer.sanitize(option.attributeName());
    }

    /** The name of the option as a {@code @property}. Must be different from {@link #fieldName}. */
    static String propertyName(PythonField option) {
        return PythonIdentifierSanitizer.sanitize(option.attributeName());
    }

    /** The name of the option as a field. */
    static String fieldName(PythonField option) {
        return "_" + PythonIdentifierSanitizer.sanitize(option.attributeName(), PROTECTED_FIELDS);
    }

    /** The name of the visitor method for the given option. */
    static String visitorMethodName(PythonField option) {
        return "_" + option.attributeName();
    }

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("class %s(ConjureUnionType):", className()));
        poetWriter.increaseIndent();
        docs().ifPresent(docs -> {
            poetWriter.writeIndentedLine("\"\"\"");
            poetWriter.writeIndentedLine(docs.get().trim());
            poetWriter.writeIndentedLine("\"\"\"");
        });

        poetWriter.writeLine();

        options()
                .forEach(option ->
                        poetWriter.writeIndentedLine("%s = None # type: %s", fieldName(option), option.myPyType()));

        poetWriter.writeLine();

        // record off the options
        poetWriter.writeIndentedLine("@builtins.classmethod");
        poetWriter.writeIndentedLine("def _options(cls):");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("# type: () -> Dict[str, ConjureFieldDefinition]"); // maybe....?
        poetWriter.writeIndentedLine("return {");
        poetWriter.increaseIndent();
        for (int i = 0; i < options().size(); i++) {
            PythonField option = options().get(i);
            poetWriter.writeIndentedLine(
                    "'%s': ConjureFieldDefinition('%s', %s)%s",
                    propertyName(option),
                    option.jsonIdentifier(),
                    option.pythonType(),
                    i == options().size() - 1 ? "" : ",");
        }
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine("}");
        poetWriter.decreaseIndent();

        poetWriter.writeLine();

        // constructor
        poetWriter.writeIndentedLine(String.format(
                "def __init__(self, %s):",
                Joiner.on(", ")
                        .join(options().stream()
                                .map(PythonField::attributeName)
                                .map(PythonIdentifierSanitizer::sanitize)
                                .map(attributeName -> String.format("%s=None", attributeName))
                                .collect(Collectors.toList()))));
        poetWriter.increaseIndent();
        // check we have exactly one non-null
        poetWriter.writeIndentedLine(
                "if %s != 1:",
                Joiner.on(" + ")
                        .join(options().stream()
                                .map(option -> String.format("(%s is not None)", parameterName(option)))
                                .collect(Collectors.toList())));
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("raise ValueError('a union must contain a single member')");
        poetWriter.decreaseIndent();
        // keep track of how many non-null there are
        poetWriter.writeLine();
        // save off
        options().forEach(option -> {
            poetWriter.writeIndentedLine("if %s is not None:", parameterName(option));
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("self.%s = %s", fieldName(option), parameterName(option));
            poetWriter.writeIndentedLine("self._type = '%s'", option.jsonIdentifier());
            poetWriter.decreaseIndent();
        });
        poetWriter.decreaseIndent();

        // python @property for each member of the union
        options().forEach(option -> {
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("@property");
            poetWriter.writeIndentedLine(String.format("def %s(self):", propertyName(option)));

            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine(String.format("# type: () -> %s", option.myPyType()));
            option.docs().ifPresent(docs -> {
                poetWriter.writeIndentedLine("\"\"\"");
                poetWriter.writeIndentedLine(docs.get().trim());
                poetWriter.writeIndentedLine("\"\"\"");
            });
            poetWriter.writeIndentedLine(String.format("return self.%s", fieldName(option)));
            poetWriter.decreaseIndent();
        });

        String visitorName = String.format("%sVisitor", className());

        poetWriter.writeLine();
        poetWriter.writeIndentedLine("def accept(self, visitor):");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("# type: (%s) -> Any", visitorName);
        poetWriter.writeIndentedLine("if not isinstance(visitor, %s):", visitorName);
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine(
                "raise ValueError('{} is not an instance of %s'.format(visitor.__class__.__name__))", visitorName);
        poetWriter.decreaseIndent();
        options().forEach(option -> {
            poetWriter.writeIndentedLine("if self.type == '%s':", option.jsonIdentifier());
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("return visitor.%s(self.%s)", visitorMethodName(option), propertyName(option));
            poetWriter.decreaseIndent();
        });
        poetWriter.decreaseIndent();
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
        poetWriter.writeLine();

        // We need to generate this base class to be python 2 compatible
        String visitorBaseClass = String.format("%sBaseClass", visitorName);
        poetWriter.writeLine(String.format("%s = ABCMeta('ABC', (object,), {}) # type: Any", visitorBaseClass));

        poetWriter.writeLine();
        poetWriter.writeLine();

        poetWriter.writeIndentedLine(String.format("class %s(%s):", visitorName, visitorBaseClass));
        poetWriter.increaseIndent();
        options().forEach(option -> {
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("@abstractmethod");
            poetWriter.writeIndentedLine("def %s(self, %s):", visitorMethodName(option), parameterName(option));
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("# type: (%s) -> Any", option.myPyType());
            poetWriter.writeIndentedLine("pass");
            poetWriter.decreaseIndent();
        });
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
        poetWriter.writeLine();
    }

    class Builder extends ImmutableUnionSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
