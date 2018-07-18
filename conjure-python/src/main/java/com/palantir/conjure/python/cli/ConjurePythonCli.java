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
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.palantir.conjure.python.ConjurePythonGenerator;
import com.palantir.conjure.python.DefaultPythonFileWriter;
import com.palantir.conjure.python.GeneratorConfiguration;
import com.palantir.conjure.python.service.ServiceGeneratorPython;
import com.palantir.conjure.python.types.DefaultBeanGeneratorPython;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public final class ConjurePythonCli {
    public static final String GENERATE_COMMAND = "generate";
    private static final String CLI_NAME = "conjure-python";
    private static final String USAGE = String.format("%s %s <target> <output>", CLI_NAME, GENERATE_COMMAND);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

    private ConjurePythonCli() {}

    public static void main(String[] args) {
        CliConfiguration cliConfig = resolveCliConfiguration(args);
        BuildConfiguration buildConfig = BuildConfiguration.load();
        generate(cliConfig.target(), cliConfig.outputDirectory(),
                resolveGeneratorConfiguration(cliConfig, buildConfig));
    }

    static CliConfiguration resolveCliConfiguration(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter hf = new HelpFormatter();

        Options options = new Options();
        options.addOption(Option.builder().hasArg()
                .desc("package name")
                .longOpt(CliConfiguration.PACKAGE_NAME)
                .required()
                .argName("name").build());
        options.addOption(Option.builder().hasArg()
                .desc("semantic version of generated code")
                .longOpt(CliConfiguration.PACKAGE_VERSION)
                .required()
                .argName("version").build());
        options.addOption(Option.builder().hasArg()
                .longOpt(CliConfiguration.PACKAGE_DESCRIPTION)
                .argName("description").build());
        options.addOption(Option.builder().hasArg()
                .longOpt(CliConfiguration.PACKAGE_URL)
                .argName("url").build());
        options.addOption(Option.builder().hasArg()
                .longOpt(CliConfiguration.PACKAGE_AUTHOR)
                .argName("author").build());
        options.addOption(Option.builder()
                .desc("write conda_recipe")
                .longOpt(CliConfiguration.WRITE_CONDA_RECIPE).build());

        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("display this help message")
                .build());

        try {
            CommandLine cmd = parser.parse(options, args, false);
            if (cmd.hasOption('h')) {
                hf.printHelp(USAGE, options, true);
                System.exit(0);
            }
            String[] parsedArgs = cmd.getArgs();
            Preconditions.checkArgument(parsedArgs.length == 3 && GENERATE_COMMAND.equals(args[0]));

            return CliConfiguration.of(parsedArgs[1], parsedArgs[2], cmd.getOptions());
        } catch (ParseException | IllegalArgumentException e) {
            hf.printHelp(USAGE, options, true);
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    static GeneratorConfiguration resolveGeneratorConfiguration(CliConfiguration cliConfig,
            BuildConfiguration buildConfig) {
        return GeneratorConfiguration.builder()
                .minConjureClientVersion(buildConfig.minConjureClientVersion())
                .packageAuthor(cliConfig.packageAuthor())
                .packageDescription(cliConfig.packageDescription())
                .packageName(cliConfig.packageName())
                .packageVersion(cliConfig.packageVersion())
                .packageUrl(cliConfig.packageUrl())
                .shouldWriteCondaRecipe(cliConfig.shouldWriteCondaRecipe())
                .build();
    }

    static void generate(File target, File outputDirectory, GeneratorConfiguration config) {
        try {
            ConjureDefinition conjureDefinition = OBJECT_MAPPER.readValue(target, ConjureDefinition.class);
            ConjurePythonGenerator generator = new ConjurePythonGenerator(
                    new DefaultBeanGeneratorPython(),
                    new ServiceGeneratorPython(),
                    config);
            generator.write(conjureDefinition, new DefaultPythonFileWriter(outputDirectory.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error parsing definition: %s", e.toString()));
        }
    }
}
