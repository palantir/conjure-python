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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ConjurePythonCliTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File targetFile;

    @Before
    public void before() throws IOException {
        targetFile = folder.newFile();
    }

    @Test
    public void correctlyParseArguments() {
        String[] args = {
                ConjurePythonCli.GENERATE_COMMAND,
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                String.format("--%s=package-name", CliConfiguration.PACKAGE_NAME),
                String.format("--%s=0.0.0", CliConfiguration.PACKAGE_VERSION),
                };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .target(targetFile)
                .outputDirectory(folder.getRoot())
                .packageName("package-name")
                .packageVersion("0.0.0")
                .build();
        assertThat(ConjurePythonCli.resolveCliConfiguration(args)).isEqualTo(expectedConfiguration);
    }

    @Test
    public void throwsWhenMissingArguments() {
        String[] args = {
                String.format("--%s=package-name", CliConfiguration.PACKAGE_NAME),
                String.format("--%s=0.0.0", CliConfiguration.PACKAGE_VERSION)
                };
        assertThatThrownBy(() -> ConjurePythonCli.resolveCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void throwsWhenTargetDoesNotExist() {
        String[] args = {ConjurePythonCli.GENERATE_COMMAND, "foo", "bar",
                         String.format("--%s=package-name", CliConfiguration.PACKAGE_NAME),
                         String.format("--%s=0.0.0", CliConfiguration.PACKAGE_VERSION)};
        assertThatThrownBy(() -> ConjurePythonCli.resolveCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Target must exist and be a file");
    }

    @Test
    public void throwsWhenOutputDoesNotExist() {
        String[] args = {ConjurePythonCli.GENERATE_COMMAND, targetFile.getAbsolutePath(), "bar",
                         String.format("--%s=package-name", CliConfiguration.PACKAGE_NAME),
                         String.format("--%s=0.0.0", CliConfiguration.PACKAGE_VERSION)};
        assertThatThrownBy(() -> ConjurePythonCli.resolveCliConfiguration(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Output must exist and be a directory");
    }

    @Test
    public void generatesCode() throws Exception {
        File outputDirectory = folder.newFolder();
        CliConfiguration cliConfig = CliConfiguration.builder()
                .target(new File("src/test/resources/conjure-api.json"))
                .outputDirectory(outputDirectory)
                .packageName("conjure")
                .packageVersion("0.0.0")
                .build();
        BuildConfiguration buildConfig = BuildConfiguration.load();
        ConjurePythonCli.generate(cliConfig.target(), cliConfig.outputDirectory(),
                ConjurePythonCli.resolveGeneratorConfiguration(cliConfig, buildConfig));
        assertThat(new File(outputDirectory, "conjure/conjure_spec/__init__.py").isFile()).isTrue();
    }

    @Test
    public void throwsWhenInvalidDefinition() throws Exception {
        CliConfiguration cliConfig = CliConfiguration.builder()
                .target(folder.newFile())
                .outputDirectory(folder.newFolder())
                .packageName("package-name")
                .packageVersion("0.0.0")
                .build();
        BuildConfiguration buildConfig = BuildConfiguration.load();
        assertThatThrownBy(() -> ConjurePythonCli.generate(cliConfig.target(), cliConfig.outputDirectory(),
                ConjurePythonCli.resolveGeneratorConfiguration(cliConfig, buildConfig)))
                .isInstanceOfSatisfying(RuntimeException.class, e -> {
                    assertThat(e.getMessage()).contains("Error parsing definition");
                });
    }

    @Test
    public void makesPackageVersionPythonic() {
        String[] args = {
                ConjurePythonCli.GENERATE_COMMAND,
                targetFile.getAbsolutePath(),
                folder.getRoot().getAbsolutePath(),
                String.format("--%s=package-name", CliConfiguration.PACKAGE_NAME),
                String.format("--%s=0.0.0-dev", CliConfiguration.PACKAGE_VERSION)
        };
        CliConfiguration expectedConfiguration = CliConfiguration.builder()
                .target(targetFile)
                .outputDirectory(folder.getRoot())
                .packageName("package-name")
                .packageVersion("0.0.0_dev")
                .build();
        assertThat(ConjurePythonCli.resolveCliConfiguration(args)).isEqualTo(expectedConfiguration);
    }

    @Test
    public void loadBuildConfiguration() {
        assertThat(BuildConfiguration.load().minConjureClientVersion()).isEqualTo("1.0.0");
    }
}
