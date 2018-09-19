/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python.poet;

import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonMetaYaml extends PythonClass {

    @Override
    default String className() {
        return "";
    }

    @Override
    default String packageName() {
        return "conda_recipe";
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
            poetWriter.writeIndentedLine("script: python setup.py install "
                    + "--single-version-externally-managed --record=record.txt");
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
