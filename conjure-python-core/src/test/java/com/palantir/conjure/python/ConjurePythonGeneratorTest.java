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

package com.palantir.conjure.python;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.python.client.ClientGenerator;
import com.palantir.conjure.python.types.DefaultBeanGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.util.Strings;
import org.junit.runner.RunWith;

@ConjureSubfolderRunner.ParentFolder("src/test/resources")
@RunWith(ConjureSubfolderRunner.class)
public final class ConjurePythonGeneratorTest {

    private final ConjurePythonGenerator generator = new ConjurePythonGenerator(
            new DefaultBeanGenerator(), new ClientGenerator(),
            GeneratorConfiguration.builder()
                    .packageName("package-name")
                    .packageVersion("0.0.0")
                    .packageDescription("project description")
                    .minConjureClientVersion("1.0.0")
                    .generatorVersion("0.0.0")
                    .shouldWriteCondaRecipe(true)
                    .generateRawSource(false)
                    .build());
    private final InMemoryPythonFileWriter pythonFileWriter = new InMemoryPythonFileWriter();

    @ConjureSubfolderRunner.Test
    public void assertThatFilesRenderAsExpected(Path folder) throws IOException {
        Path expected = folder.resolve("expected");
        ConjureDefinition definition = getInputDefinitions(folder);
        maybeResetExpectedDirectory(expected, definition);

        generator.write(definition, pythonFileWriter);
        assertFoldersEqual(expected);
    }

    private void assertFoldersEqual(Path expected) throws IOException {
        Set<Path> generatedButNotExpected = pythonFileWriter.getPythonFiles().keySet();
        long count = 0;
        try (Stream<Path> walk = Files.walk(expected)) {
            for (Path path : walk.collect(Collectors.toList())) {
                if (!path.toFile().isFile()) {
                    continue;
                }
                String expectedContent = Strings.join(Files.readAllLines(path)).with("\n") + "\n";
                assertThat(pythonFileWriter.getPythonFiles().get(expected.relativize(path))).isEqualTo(expectedContent);
                generatedButNotExpected.remove(expected.relativize(path));
                count += 1;
            }
        }
        assertThat(generatedButNotExpected).isEmpty();
        System.out.println(count + " files checked");
    }

    private void maybeResetExpectedDirectory(Path expected, ConjureDefinition definition) throws IOException {
        if (Boolean.valueOf(System.getProperty("recreate", "false"))
                || !expected.toFile().isDirectory()) {
            Files.createDirectories(expected);
            try (Stream<Path> walk = Files.walk(expected)) {
                walk.filter(path -> path.toFile().isFile()).forEach(path -> path.toFile().delete());
            }
            try (Stream<Path> walk = Files.walk(expected)) {
                walk.forEach(path -> path.toFile().delete());
            }
            Files.createDirectories(expected);

            generator.write(definition, new DefaultPythonFileWriter(expected));
        }
    }

    private ConjureDefinition getInputDefinitions(Path folder) throws IOException {
        Files.createDirectories(folder);
        try (Stream<Path> walk = Files.walk(folder)) {
            List<File> files = walk
                    .map(Path::toFile)
                    .filter(file -> file.toString().endsWith(".yml"))
                    .collect(Collectors.toList());

            if (files.isEmpty()) {
                throw new RuntimeException(
                        folder + " contains no conjure.yml files, please write one to set up a new test");
            }

            return Conjure.parse(files);
        }
    }
}
