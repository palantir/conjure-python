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
public interface PythonMetaYaml extends PythonSnippet {
    @Override
    default String idForSorting() {
        return "metaYaml";
    }

    String condaPackageName();

    String packageVersion();

    List<String> installDependencies();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            poetWriter.writeIndentedLine("package:");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("name: %s", condaPackageName());
            poetWriter.writeIndentedLine("version: %s", packageVersion());
            poetWriter.decreaseIndent();

            poetWriter.writeLine();

            poetWriter.writeIndentedLine("source:");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("path: ../");
            poetWriter.decreaseIndent();

            poetWriter.writeLine();

            poetWriter.writeIndentedLine("build:");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("noarch: python");
            poetWriter.writeIndentedLine(
                    "script: python setup.py install " + "--single-version-externally-managed --record=record.txt");
            poetWriter.decreaseIndent();

            poetWriter.writeLine();

            poetWriter.writeIndentedLine("requirements:");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("build:");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("- python");
            poetWriter.writeIndentedLine("- setuptools");
            installDependencies().forEach(dependency -> {
                poetWriter.writeIndentedLine("- %s", dependency);
            });
            poetWriter.decreaseIndent();
            poetWriter.writeLine();
            poetWriter.writeIndentedLine("run:");
            poetWriter.increaseIndent();
            poetWriter.writeIndentedLine("- python");
            installDependencies().forEach(dependency -> {
                poetWriter.writeIndentedLine("- %s", dependency);
            });
            poetWriter.decreaseIndent();
            poetWriter.decreaseIndent();
        });
    }

    class Builder extends ImmutablePythonMetaYaml.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
