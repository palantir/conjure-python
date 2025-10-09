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
import com.palantir.conjure.spec.ConjureDefinition;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public final class PreserveFieldOrderTest {

    @Test
    public void testFieldOrderIsPreserved() throws IOException {
        String generated = generateCode(true);

        // With preserveFieldOrder=true: zebra, apple, middle, banana
        assertThat(generated.indexOf("zebra:")).isLessThan(generated.indexOf("apple:"));
        assertThat(generated.indexOf("apple:")).isLessThan(generated.indexOf("middle:"));
        assertThat(generated.indexOf("middle:")).isLessThan(generated.indexOf("banana:"));
    }

    @Test
    public void testFieldOrderIsAlphabeticalByDefault() throws IOException {
        String generated = generateCode(false);

        // Default: apple, banana, middle, zebra (alphabetical)
        assertThat(generated.indexOf("apple:")).isLessThan(generated.indexOf("banana:"));
        assertThat(generated.indexOf("banana:")).isLessThan(generated.indexOf("middle:"));
        assertThat(generated.indexOf("middle:")).isLessThan(generated.indexOf("zebra:"));
    }

    @SuppressWarnings("for-rollout:deprecation")
    private String generateCode(boolean preserveFieldOrder) throws IOException {
        ConjurePythonGenerator generator = new ConjurePythonGenerator(GeneratorConfiguration.builder()
                .packageName("test")
                .packageVersion("0.0.0")
                .minConjureClientVersion("2.8.0")
                .generatorVersion("0.0.0")
                .shouldWriteCondaRecipe(false)
                .generateRawSource(false)
                .preserveFieldOrder(preserveFieldOrder)
                .build());

        Path testFolder = Path.of("src/test/resources/preserve-field-order");
        List<File> files;
        try (Stream<Path> walk = Files.walk(testFolder)) {
            files = walk.map(Path::toFile)
                    .filter(file -> file.toString().endsWith(".yml"))
                    .collect(Collectors.toList());
        }
        ConjureDefinition definition = Conjure.parse(files);

        InMemoryPythonFileWriter writer = new InMemoryPythonFileWriter();
        generator.write(definition, writer);

        return writer.getPythonFiles().values().stream()
                .filter(content -> content.contains("def __init__"))
                .findFirst()
                .orElseThrow();
    }
}
