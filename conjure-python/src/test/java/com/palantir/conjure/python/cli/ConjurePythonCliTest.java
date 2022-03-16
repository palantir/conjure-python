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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

public class ConjurePythonCliTest {

    @TempDir
    public Path folder;

    private Path inputFile;

    @BeforeEach
    public void before() throws IOException {
        inputFile = folder.resolve("input-file");
    }

    @Test
    public void correctlyParseArguments() {
        String[] args = {
            "generate",
            inputFile.toAbsolutePath().toString(),
            folder.toAbsolutePath().toString(),
            "--packageName",
            "package-name",
            "--packageVersion",
            "0.0.0",
        };
        ConjurePythonCli.GenerateCommand cmd = new CommandLine(new ConjurePythonCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(inputFile)
                .output(folder.getRoot())
                .packageName("package-name")
                .packageVersion("0.0.0")
                .build();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void throwsWhenTargetDoesNotExist() {
        String[] args = {"generate", "foo", "bar", "--rawSource"};
        ConjurePythonCli.GenerateCommand cmd = new CommandLine(new ConjurePythonCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        assertThatThrownBy(cmd::getConfiguration)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Target must exist and be a file");
    }

    @Test
    public void throwsWhenOutputDoesNotExist() {
        String[] args = {"generate", inputFile.toAbsolutePath().toString(), "bar", "--rawSource"};
        ConjurePythonCli.GenerateCommand cmd = new CommandLine(new ConjurePythonCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        assertThatThrownBy(cmd::getConfiguration)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Output must exist and be a directory");
    }

    @Test
    public void makesPackageVersionPythonic() {
        String[] args = {
            "generate",
            inputFile.toAbsolutePath().toString(),
            folder.toAbsolutePath().toString(),
            "--packageName=package-name",
            "--packageVersion=0.0.0-dev"
        };
        ConjurePythonCli.GenerateCommand cmd = new CommandLine(new ConjurePythonCli())
                .parseArgs(args)
                .asCommandLineList()
                .get(1)
                .getCommand();
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .input(inputFile)
                .output(folder.getRoot())
                .packageName("package-name")
                .packageVersion("0.0.0_dev")
                .build();
        assertThat(cmd.getConfiguration()).isEqualTo(expectedConfiguration);
    }

    @Test
    public void generatesCode(@TempDir Path output) throws Exception {
        String[] args = {
            "generate",
            "src/test/resources/conjure-api.json",
            output.toAbsolutePath().toString(),
            "--packageName",
            "conjure",
            "--packageVersion",
            "0.0.0"
        };
        assertThat(new CommandLine(new ConjurePythonCli()).execute(args)).isZero();
        assertThat(Files.isRegularFile(output.resolve("conjure/conjure_spec/__init__.py")))
                .isTrue();
    }

    @Test
    public void generatesRawSource(@TempDir Path output) throws IOException {
        String[] args = {
            "generate",
            "src/test/resources/conjure-api.json",
            output.toAbsolutePath().toString(),
            "--rawSource",
        };
        assertThat(new CommandLine(new ConjurePythonCli()).execute(args)).isZero();
        assertThat(Files.isRegularFile(output.resolve("conjure_spec/__init__.py")))
                .isTrue();
    }

    @Test
    public void throwsWhenInvalidDefinition(@TempDir Path output, @TempDir File file) {
        String[] args = {
            "generate", file.getAbsolutePath(), output.toAbsolutePath().toString(), "--rawSource",
        };

        AtomicReference<Exception> executionException = new AtomicReference<>();
        new CommandLine(new ConjurePythonCli())
                .setExecutionExceptionHandler((ex, _commandLine, _parseResult) -> {
                    executionException.set(ex);
                    throw ex;
                })
                .execute(args);
        assertThat(executionException.get())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error parsing definition");
    }

    @Test
    public void loadBuildConfiguration() {
        assertThat(BuildConfiguration.load().minConjureClientVersion()).isEqualTo("2.1.0");
    }
}
