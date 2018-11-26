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

import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface UnionVisitorSnippet extends PythonSnippet {
    PythonImport ABC_IMPORTS = PythonImport.builder()
            .moduleSpecifier("abc")
            .addNamedImports("ABCMeta", "abstractmethod")
            .build();

    @Override
    @Value.Default
    default String idForSorting() {
        return String.format("%s", className());
    }

    String className();

    List<PythonField> options();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("class %s(ABCMeta('ABC', (object,), {})):", className()));
        poetWriter.increaseIndent();
        options().forEach(option -> {
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("@abstractmethod");
            poetWriter.writeIndentedLine("def _%s(self, %s):", option.attributeName(),
                    PythonIdentifierSanitizer.sanitize(option.attributeName()));
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("# type: (%s) -> Any", option.myPyType());
            poetWriter.writeIndentedLine("pass");
            poetWriter.decreaseIndent();
        });
        poetWriter.decreaseIndent();
        poetWriter.writeLine();
        poetWriter.writeLine();
    }

    class Builder extends ImmutableUnionVisitorSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
