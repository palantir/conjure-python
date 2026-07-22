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
                    .addNamedImports(NamedImport.of("ConjureEncoder"))
                    .addNamedImports(NamedImport.of("ConjureDecoder"))
                    .build(),
            PythonImport.of("builtins"),
            PythonImport.of("uuid"),
            PythonImport.builder()
                    .moduleSpecifier(ImportTypeVisitor.TYPING)
                    .addNamedImports(NamedImport.of("Any"))
                    .addNamedImports(NamedImport.of("Dict"))
                    .addNamedImports(NamedImport.of("Optional"))
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

    default List<PythonField> args() {
        return ImmutableList.<PythonField>builder()
                .addAll(safeArgs())
                .addAll(unsafeArgs())
                .build();
    }

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("class %s(Exception):", className()));
        poetWriter.increaseIndent();
        docs().ifPresent(poetWriter::writeDocs);

        poetWriter.writeLine();

        poetWriter.writeIndentedLine(String.format("ERROR_CODE = \"%s\"", errorCode()));
        poetWriter.writeIndentedLine(String.format("ERROR_NAMESPACE = \"%s\"", namespace()));
        poetWriter.writeIndentedLine(String.format("ERROR_NAME = \"%s:%s\"", namespace(), definitionName()));

        poetWriter.writeLine();

        emitConstructor(poetWriter);
        if (!args().isEmpty()) {
            emitProperties(poetWriter);
        }
        emitEncode(poetWriter);
        emitDecode(poetWriter);

        poetWriter.decreaseIndent();
        poetWriter.writeLine();
        poetWriter.writeLine();

        PythonClassRenamer.renameClass(poetWriter, className(), definitionPackage(), definitionName());
    }

    default void emitConstructor(PythonPoetWriter poetWriter) {
        StringBuilder signature = new StringBuilder("def __init__(self");
        for (PythonField field : args()) {
            signature.append(String.format(
                    ", %s: %s", PythonIdentifierSanitizer.sanitize(field.attributeName()), field.myPyType()));
        }
        signature.append(", error_instance_id: Optional[str] = None) -> None:");
        poetWriter.writeIndentedLine(signature.toString());

        poetWriter.increaseIndent();
        for (PythonField field : args()) {
            String attribute = PythonIdentifierSanitizer.sanitize(field.attributeName());
            poetWriter.writeIndentedLine(String.format("self._%s = %s", attribute, attribute));
        }
        poetWriter.writeIndentedLine("self.error_instance_id = error_instance_id if error_instance_id is not None "
                + "else str(uuid.uuid4())");
        poetWriter.writeIndentedLine("super().__init__(self.ERROR_NAME)");
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
    }

    default void emitProperties(PythonPoetWriter poetWriter) {
        for (PythonField field : args()) {
            String attribute = PythonIdentifierSanitizer.sanitize(field.attributeName());
            poetWriter.writeIndentedLine("@builtins.property");
            poetWriter.writeIndentedLine(String.format("def %s(self) -> %s:", attribute, field.myPyType()));
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine(String.format("return self._%s", attribute));
            poetWriter.decreaseIndent();
            poetWriter.writeLine();
        }
    }

    default void emitEncode(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine("def encode(self) -> Dict[str, Any]:");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("return {");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("'errorCode': self.ERROR_CODE,");
        poetWriter.writeIndentedLine("'errorName': self.ERROR_NAME,");
        poetWriter.writeIndentedLine("'errorInstanceId': self.error_instance_id,");
        poetWriter.writeIndentedLine("'parameters': {");
        poetWriter.increaseIndent();
        List<PythonField> args = args();
        for (int i = 0; i < args.size(); i++) {
            PythonField field = args.get(i);
            String comma = i == args.size() - 1 ? "" : ",";
            poetWriter.writeIndentedLine(String.format(
                    "'%s': ConjureEncoder.do_encode(self.%s)%s",
                    field.jsonIdentifier(), PythonIdentifierSanitizer.sanitize(field.attributeName()), comma));
        }
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine("}");
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine("}");
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
    }

    default void emitDecode(PythonPoetWriter poetWriter) {
        List<PythonField> args = args();
        poetWriter.writeIndentedLine("@builtins.classmethod");
        poetWriter.writeIndentedLine(String.format("def decode(cls, error: Dict[str, Any]) -> '%s':", className()));
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine("if error.get('errorName') != cls.ERROR_NAME:");
        poetWriter.increaseIndent();
        poetWriter.writeIndentedLine(
                "raise ValueError(f\"Error '{error.get('errorName')}' is not a {cls.ERROR_NAME}\")");
        poetWriter.decreaseIndent();

        if (!args.isEmpty()) {
            poetWriter.writeIndentedLine("decoder = ConjureDecoder()");
            poetWriter.writeIndentedLine("parameters = error.get('parameters', {})");
        }

        poetWriter.writeIndentedLine("return cls(");
        poetWriter.increaseIndent();
        for (PythonField field : args) {
            poetWriter.writeIndentedLine(String.format(
                    "%s=decoder.decode(parameters.get('%s'), %s),",
                    PythonIdentifierSanitizer.sanitize(field.attributeName()),
                    field.jsonIdentifier(),
                    field.pythonType()));
        }
        poetWriter.writeIndentedLine("error_instance_id=error.get('errorInstanceId')");
        poetWriter.decreaseIndent();
        poetWriter.writeIndentedLine(")");
        poetWriter.decreaseIndent();
    }

    class Builder extends ImmutableErrorSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
