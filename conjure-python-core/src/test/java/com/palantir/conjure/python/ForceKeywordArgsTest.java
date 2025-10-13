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

package com.palantir.conjure.python;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public final class ForceKeywordArgsTest {

    @Test
    public void testConstructorKeywordArgsAreForced() throws IOException {
        String generated = generateCode(true);

        assertThat(generated).contains("def __init__(self, *, age: int, email: str, name: str) -> None:");
    }

    @Test
    public void testConstructorPositionalArgsByDefault() throws IOException {
        String generated = generateCode(false);

        assertThat(generated).contains("def __init__(self, age: int, email: str, name: str) -> None:");
        assertThat(generated).doesNotContain("def __init__(self, *,");
    }

    @Test
    public void testUnionKeywordArgsAreForced() throws IOException {
        String generated = generateCode(true);
        // The signature spans multiple lines with specific indentation
        assertThat(generated).contains("    def __init__(\n            self,\n            *,");
    }

    @Test
    public void testUnionPositionalArgsByDefault() throws IOException {
        String generated = generateCode(false);

        // The signature spans multiple lines with specific indentation
        assertThat(generated).contains("    def __init__(\n            self,\n            foo: Optional[str] = None,");
        assertThat(generated).doesNotContain("    def __init__(\n            self,\n            *,");
    }

    @Test
    public void testServiceEndpointKeywordArgsAreForced() throws IOException {
        String generated = generateCode(true);
        assertThat(generated).contains("def test_endpoint(self, *, param1: str, param2: int) -> str:");
    }

    @Test
    public void testServiceEndpointPositionalArgsByDefault() throws IOException {
        String generated = generateCode(false);
        assertThat(generated).contains("def test_endpoint(self, param1: str, param2: int) -> str:");
        assertThat(generated).doesNotContain("def test_endpoint(self, *,");
    }

    @SuppressWarnings("for-rollout:deprecation")
    private String generateCode(boolean forceKeywordArgs) throws IOException {
        ConjurePythonGenerator generator = new ConjurePythonGenerator(GeneratorConfiguration.builder()
                .packageName("test")
                .packageVersion("0.0.0")
                .minConjureClientVersion("2.8.0")
                .generatorVersion("0.0.0")
                .shouldWriteCondaRecipe(false)
                .generateRawSource(false)
                .forceKeywordArgs(forceKeywordArgs)
                .build());

        Path testFolder = Path.of("src/test/resources/force-keyword-args");
        List<File> files;
        try (Stream<Path> walk = Files.walk(testFolder)) {
            files = walk.map(Path::toFile)
                    .filter(file -> file.toString().endsWith(".yml"))
                    .collect(Collectors.toList());
        }
        ConjureDefinition definition = Conjure.parse(files);

        InMemoryPythonFileWriter writer = new InMemoryPythonFileWriter();
        generator.write(definition, writer);

        return writer.getPythonFiles().values().stream().collect(Collectors.joining("\n"));
    }
}
