/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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
import com.palantir.conjure.python.processors.PythonIdentifierSanitizer;
import com.palantir.conjure.python.types.ImportTypeVisitor;
import com.palantir.conjure.spec.Documentation;
import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface ErrorSnippet extends PythonSnippet {
    ImmutableList<PythonImport> DEFAULT_IMPORTS = ImmutableList.of(
            PythonImport.builder()
                    .moduleSpecifier(ImportTypeVisitor.CONJURE_PYTHON_CLIENT)
                    .addNamedImports(NamedImport.of("ConjureHTTPError"))
                    .build(),
            PythonImport.of("builtins"),
            PythonImport.builder()
                    .moduleSpecifier(ImportTypeVisitor.TYPING)
                    .addNamedImports(NamedImport.of("TypedDict"))
                    .build());

    @Override
    @Value.Default
    default String idForSorting() {
        return className();
    }

    String className();

    String definitionName();

    PythonPackage definitionPackage();

    Optional<Documentation> docs();

    String errorCode();

    String namespace();

    List<PythonField> safeArgs();

    List<PythonField> unsafeArgs();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("class %s(ConjureHTTPError):", className()));
        poetWriter.increaseIndent();
        docs().ifPresent(poetWriter::writeDocs);

        poetWriter.writeLine();

        // Error constants. ERROR_NAME is the fully-qualified wire form (e.g. "Datasets:DatasetNotFound") and
        // matches the value of ConjureHTTPError.error_name parsed from the response body.
        poetWriter.writeIndentedLine(String.format("ERROR_CODE = \"%s\"", errorCode()));
        poetWriter.writeIndentedLine(String.format("ERROR_NAMESPACE = \"%s\"", namespace()));
        poetWriter.writeIndentedLine(String.format("ERROR_NAME = \"%s:%s\"", namespace(), definitionName()));

        poetWriter.writeLine();

        // args
        emitTypedDict(poetWriter, "SafeArgs", safeArgs());
        emitTypedDict(poetWriter, "UnsafeArgs", unsafeArgs());

        emitConstructor(poetWriter);

        // classmethods
        emitIsInstanceMethod(poetWriter);
        emitFromErrorMethod(poetWriter);

        // end of class def
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
        poetWriter.writeLine();

        PythonClassRenamer.renameClass(poetWriter, className(), definitionPackage(), definitionName());
    }

    default void emitTypedDict(PythonPoetWriter poetWriter, String typedDictName, List<PythonField> fields) {
        if (fields.isEmpty()) {
            return;
        }
        poetWriter.writeIndentedLine(String.format("class %s(TypedDict):", typedDictName));
        poetWriter.increaseIndent();
        for (PythonField field : fields) {
            poetWriter.writeIndentedLine(String.format(
                    "%s: %s", PythonIdentifierSanitizer.sanitize(field.attributeName()), field.myPyType()));
        }
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
    }

    default void emitConstructor(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine("def __init__(self, base_error: ConjureHTTPError) -> None:");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("super().__init__(");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("status_code=base_error.status_code,");
        // TODO(bzhang): Use enum once https://github.com/palantir/conjure-python-client/pull/171 is merged
        poetWriter.writeIndentedLine("error_code=base_error.error_code,");
        poetWriter.writeIndentedLine("error_name=base_error.error_name,");
        poetWriter.writeIndentedLine("error_instance_id=base_error.error_instance_id,");
        poetWriter.writeIndentedLine("parameters=base_error.parameters");
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine(")");

        emitArgsParser(poetWriter, "safe_args", "SafeArgs", safeArgs());
        emitArgsParser(poetWriter, "unsafe_args", "UnsafeArgs", unsafeArgs());

        poetWriter.decreaseIndent();
        poetWriter.writeLine();
    }

    default void emitArgsParser(
            PythonPoetWriter poetWriter, String fieldName, String typeName, List<PythonField> fields) {
        if (fields.isEmpty()) {
            return;
        }
        poetWriter.writeIndentedLine(String.format("self.%s: %s.%s = {", fieldName, className(), typeName));
        poetWriter.increaseIndent();
        for (int i = 0; i < fields.size(); i++) {
            PythonField field = fields.get(i);
            String comma = i == fields.size() - 1 ? "" : ",";
            String lookup = field.isOptional()
                    ? String.format("base_error.parameters.get('%s')", field.jsonIdentifier())
                    : String.format("base_error.parameters['%s']", field.jsonIdentifier());
            poetWriter.writeIndentedLine(String.format(
                    "'%s': %s%s", PythonIdentifierSanitizer.sanitize(field.attributeName()), lookup, comma));
        }
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine("}");
    }

    default void emitIsInstanceMethod(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine("@builtins.classmethod");
        poetWriter.writeIndentedLine("def is_instance(cls, error: ConjureHTTPError) -> bool:");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("return (");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("error.error_name == cls.ERROR_NAME and");
        poetWriter.writeIndentedLine("error.error_code == cls.ERROR_CODE");
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine(")");
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
    }

    default void emitFromErrorMethod(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine("@builtins.classmethod");
        poetWriter.writeIndentedLine(
                String.format("def from_error(cls, error: ConjureHTTPError) -> '%s':", className()));
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("if not cls.is_instance(error):");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("raise ValueError(f\"Error '{error.error_name}' is not a {cls.ERROR_NAME}\")");
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine("return cls(error)");
        poetWriter.decreaseIndent();
    }

    class Builder extends ImmutableErrorSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
