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

package com.palantir.conjure.python.cli;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.annotations.VisibleForTesting;
import com.palantir.conjure.python.ConjurePythonGenerator;
import com.palantir.conjure.python.DefaultPythonFileWriter;
import com.palantir.conjure.python.GeneratorConfiguration;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import picocli.CommandLine;

@CommandLine.Command(
        name = "conjure-python",
        description = "CLI to generate Python Classes and interfaces from Conjure API definitions.",
        mixinStandardHelpOptions = true,
        subcommands = {ConjurePythonCli.GenerateCommand.class})
public final class ConjurePythonCli implements Runnable {
    public static void main(String[] args) {
        CommandLine.run(new ConjurePythonCli(), args);
    }

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    @CommandLine.Command(
            name = "generate",
            description = "Generate Python bindings for a Conjure API",
            mixinStandardHelpOptions = true,
            usageHelpWidth = 120)
    public static final class GenerateCommand implements Runnable {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

        @CommandLine.Parameters(paramLabel = "<input>", description = "Path to the input IR file", index = "0")
        private String input;

        @CommandLine.Parameters(
                paramLabel = "<output>",
                description = "Output directory for generated source",
                index = "1")
        private String output;

        @CommandLine.Option(names = "--packageName", description = "The name of the package to generate.")
        private String packageName;

        @CommandLine.Option(names = "--packageVersion", description = "The version of the package to generate.")
        private String packageVersion;

        @CommandLine.Option(names = "--packageDescription", description = "The description of the package to generate.")
        private String packageDescription;

        @CommandLine.Option(names = "--packageUrl", description = "The url of the package to generate")
        private String packageUrl;

        @CommandLine.Option(
                names = "--rawSource",
                defaultValue = "false",
                description = "Only generate the plain source without any package metadata")
        private boolean rawSource;

        @CommandLine.Option(
                names = "--writeCondaRecipe",
                defaultValue = "false",
                description = "Generate a `conda_recipe/meta.yaml`")
        private boolean writeCondaRecipe;

        @CommandLine.Option(
                names = "--minimumPythonVersion",
                defaultValue = "3",
                description = "generated code must be usable on this python version (i.e. ASCII only source for "
                        + "python 2)")
        private int minimumPythonVersion;

        @CommandLine.Unmatched
        @SuppressWarnings("StrictUnusedVariable")
        private List<String> unmatchedOptions;

        @Override
        public void run() {
            CliConfiguration cliConfig = getConfiguration();
            GeneratorConfiguration generatorConfig =
                    resolveGeneratorConfiguration(cliConfig, BuildConfiguration.load());
            try {
                ConjureDefinition conjureDefinition = OBJECT_MAPPER.readValue(new File(input), ConjureDefinition.class);
                ConjurePythonGenerator generator = new ConjurePythonGenerator(generatorConfig);
                generator.write(
                        conjureDefinition,
                        new DefaultPythonFileWriter(Paths.get(output), cliConfig.minimumPythonVersion()));
            } catch (IOException e) {
                throw new RuntimeException(String.format("Error parsing definition: %s", e.toString()));
            }
        }

        @VisibleForTesting
        CliConfiguration getConfiguration() {
            return CliConfiguration.builder()
                    .input(new File(input))
                    .output(new File(output))
                    .packageName(Optional.ofNullable(packageName))
                    .packageVersion(Optional.ofNullable(packageVersion).map(version -> version.replace('-', '_')))
                    .packageDescription(Optional.ofNullable(packageDescription))
                    .packageUrl(Optional.ofNullable(packageUrl))
                    .generateRawSource(rawSource)
                    .shouldWriteCondaRecipe(writeCondaRecipe)
                    .minimumPythonVersion(minimumPythonVersion)
                    .build();
        }

        static GeneratorConfiguration resolveGeneratorConfiguration(
                CliConfiguration cliConfig, BuildConfiguration buildConfig) {
            return GeneratorConfiguration.builder()
                    .generatorVersion(buildConfig.generatorVersion())
                    .minConjureClientVersion(buildConfig.minConjureClientVersion())
                    .packageAuthor(cliConfig.packageAuthor())
                    .packageDescription(cliConfig.packageDescription())
                    .packageName(cliConfig.packageName())
                    .packageVersion(cliConfig.packageVersion())
                    .packageUrl(cliConfig.packageUrl())
                    .shouldWriteCondaRecipe(cliConfig.shouldWriteCondaRecipe())
                    .generateRawSource(cliConfig.generateRawSource())
                    .build();
        }
    }
}
