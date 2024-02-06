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
import com.palantir.conjure.python.processors.PythonIdentifierSanitizer;
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
                    .addNamedImports(NamedImport.of("ConjureUnionType"), NamedImport.of("ConjureFieldDefinition"))
                    .build(),
            PythonImport.builder()
                    .moduleSpecifier("abc")
                    .addNamedImports(NamedImport.of("abstractmethod"))
                    .build(),
            PythonImport.builder()
                    .moduleSpecifier(ImportTypeVisitor.TYPING)
                    .addNamedImports(NamedImport.of("Dict"), NamedImport.of("Any"), NamedImport.of("Optional"))
                    .build(),
            PythonImport.of("builtins"));
    ImmutableSet<String> PROTECTED_FIELDS = ImmutableSet.of("options");

    @Override
    @Value.Default
    default String idForSorting() {
        return className();
    }

    String className();

    String definitionName();

    PythonPackage definitionPackage();

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
    @SuppressWarnings("checkstyle:methodlength")
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            poetWriter.writeIndentedLine(String.format("class %s(ConjureUnionType):", className()));
            poetWriter.increaseIndent();
            docs().ifPresent(poetWriter::writeDocs);

            options()
                    .forEach(option -> poetWriter.writeIndentedLine(
                            "%s: Optional[%s] = None", fieldName(option), option.myPyType()));

            poetWriter.writeLine();

            // record off the options
            poetWriter.writeIndentedLine("@builtins.classmethod");
            poetWriter.writeIndentedLine("def _options(cls) -> Dict[str, ConjureFieldDefinition]:");
            poetWriter.increaseIndent();
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
            poetWriter.writeIndentedLine("def __init__(");
            poetWriter.increaseIndent();
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("self,");
            for (int i = 0; i < options().size(); i++) {
                PythonField option = options().get(i);

                poetWriter.writeIndentedLine(String.format(
                        "%s: Optional[%s] = None,",
                        PythonIdentifierSanitizer.sanitize(option.attributeName()), option.myPyType()));
            }
            poetWriter.writeIndentedLine("type_of_union: Optional[str] = None");
            poetWriter.writeIndentedLine(") -> None:");
            poetWriter.decreaseIndent();

            // --back-compat to determine union type if type_of_union isn't passed in--
            // check we have exactly one non-null
            poetWriter.writeIndentedLine("if type_of_union is None:");
            poetWriter.increaseIndent();
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
            poetWriter.writeLine();

            // --proper way of determining union type using type_of_union--
            // save off
            options().forEach(option -> {
                poetWriter.writeIndentedLine("elif type_of_union == '%s':", option.jsonIdentifier());
                poetWriter.increaseIndent();

                if (!parameterName(option).equals("optional")) {
                    poetWriter.writeIndentedLine("if %s is None:", parameterName(option));
                    poetWriter.increaseIndent();
                    poetWriter.writeIndentedLine("raise ValueError('a union value must not be None')");
                    poetWriter.decreaseIndent();
                }

                poetWriter.writeIndentedLine("self.%s = %s", fieldName(option), parameterName(option));
                poetWriter.writeIndentedLine("self._type = '%s'", option.jsonIdentifier());
                poetWriter.decreaseIndent();
            });
            poetWriter.decreaseIndent();

            // python @builtins.property for each member of the union
            options().stream()
                    .filter(option -> !propertyName(option).equals("float"))
                    .forEach(option -> emitProperty(poetWriter, option));

            // properties with builtin names must come after other properties
            // can be removed once these names are properly sanitized but
            // that will require a breaking change and major version bump
            options().stream()
                    .filter(option -> propertyName(option).equals("float"))
                    .forEach(option -> emitProperty(poetWriter, option));

            String visitorName = String.format("%sVisitor", className());
            String definitionVisitorName = String.format("%sVisitor", definitionName());

            poetWriter.writeLine();
            poetWriter.writeIndentedLine("def accept(self, visitor) -> Any:");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("if not isinstance(visitor, %s):", visitorName);
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine(
                    "raise ValueError('{} is not an instance of %s'.format(visitor.__class__.__name__))", visitorName);
            poetWriter.decreaseIndent();
            options().forEach(option -> {
                if (parameterName(option).equals("optional")) {
                    poetWriter.writeIndentedLine("if self._type == '%s':", parameterName(option));
                } else {
                    poetWriter.writeIndentedLine(
                            "if self._type == '%s' and self.%s is not None:",
                            option.jsonIdentifier(), propertyName(option));
                }
                poetWriter.increaseIndent();
                poetWriter.writeIndentedLine(
                        "return visitor.%s(self.%s)", visitorMethodName(option), propertyName(option));
                poetWriter.decreaseIndent();
            });
            poetWriter.decreaseIndent();
            poetWriter.decreaseIndent();
            poetWriter.writeLine();
            poetWriter.writeLine();

            PythonClassRenamer.renameClass(poetWriter, className(), definitionPackage(), definitionName());

            poetWriter.writeIndentedLine(String.format("class %s:", visitorName));
            poetWriter.increaseIndent();
            options().forEach(option -> {
                poetWriter.writeLine();
                poetWriter.writeIndentedLine("@abstractmethod");
                poetWriter.writeIndentedLine(
                        "def %s(self, %s: %s) -> Any:",
                        visitorMethodName(option), parameterName(option), option.myPyType());
                poetWriter.increaseIndent();
                poetWriter.writeIndentedLine("pass");
                poetWriter.decreaseIndent();
            });
            poetWriter.decreaseIndent();
            poetWriter.writeLine();
            poetWriter.writeLine();

            PythonClassRenamer.renameClass(poetWriter, visitorName, definitionPackage(), definitionVisitorName);
        });
    }

    private static void emitProperty(PythonPoetWriter poetWriter, PythonField option) {
        poetWriter.writeLine();

        poetWriter.writeIndentedLine("@builtins.property");
        poetWriter.writeIndentedLine(
                String.format("def %s(self) -> Optional[%s]:", propertyName(option), option.myPyType()));

        poetWriter.increaseIndent();
        option.docs().ifPresent(poetWriter::writeDocs);
        poetWriter.writeIndentedLine(String.format("return self.%s", fieldName(option)));
        poetWriter.decreaseIndent();
    }

    class Builder extends ImmutableUnionSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
