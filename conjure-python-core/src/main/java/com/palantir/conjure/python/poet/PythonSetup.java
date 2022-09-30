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

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonSetup extends PythonSnippet {
    ImmutableSet<PythonImport> DEFAULT_IMPORTS = ImmutableSet.of(PythonImport.builder()
            .moduleSpecifier("setuptools")
            .addNamedImports(NamedImport.of("find_packages"), NamedImport.of("setup"))
            .build());

    @Override
    @Value.Default
    default String idForSorting() {
        return "setup";
    }

    @Override
    @Value.Default
    default Set<PythonImport> imports() {
        return DEFAULT_IMPORTS;
    }

    Map<String, String> options();

    /**
     * Options that aren't a string value and as such shouldn't be wrapped in quotes.
     */
    Map<String, String> rawOptions();

    List<String> installDependencies();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            poetWriter.writeIndentedLine("setup(");
            poetWriter.increaseIndent();

            options().forEach((key, value) -> {
                poetWriter.writeIndentedLine("%s='%s',", key, value);
            });
            rawOptions().forEach((key, value) -> {
                poetWriter.writeIndentedLine("%s=%s,", key, value);
            });
            poetWriter.writeIndentedLine("python_requires='>=3.8',");
            poetWriter.writeIndentedLine("packages=find_packages(),");

            poetWriter.writeIndentedLine("install_requires=[");
            poetWriter.increaseIndent();
            installDependencies().forEach(dependency -> {
                poetWriter.writeIndentedLine("'%s',", dependency);
            });
            poetWriter.decreaseIndent();
            poetWriter.writeIndentedLine("],");

            poetWriter.decreaseIndent();
            poetWriter.writeIndentedLine(")");
        });
    }

    class Builder extends ImmutablePythonSetup.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
