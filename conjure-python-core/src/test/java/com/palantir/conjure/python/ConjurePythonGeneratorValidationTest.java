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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.EnumValueDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import java.util.List;
import org.junit.Test;

/**
 * Verifies that {@link ConjurePythonGenerator} validates IR-supplied identifiers before emitting Python source.
 *
 * <p>The definitions here are built directly via the spec builders rather than through {@code Conjure.parse}: the
 * Conjure compiler validates identifiers while parsing, so an invalid identifier only reaches the generator when a
 * definition is constructed (or deserialized) without going through the compiler.
 */
public final class ConjurePythonGeneratorValidationTest {

    private static final TypeName ENUM_NAME = TypeName.of("Color", "com.example.product");

    private final ConjurePythonGenerator generator = new ConjurePythonGenerator(GeneratorConfiguration.builder()
            .packageName("package-name")
            .packageVersion("0.0.0")
            .minConjureClientVersion("2.8.0")
            .maxConjureClientVersion("4")
            .generatorVersion("0.0.0")
            .shouldWriteCondaRecipe(false)
            .generateRawSource(false)
            .build());

    @Test
    public void rejectsEnumValueThatIsNotAValidIdentifier() {
        // An enum value outside the enum-identifier grammar would otherwise be emitted verbatim as the left-hand
        // side of a `NAME = 'NAME'` assignment, with the trailing " #" commenting out the rest of the line.
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(List.of(TypeDefinition.enum_(EnumDefinition.builder()
                        .typeName(ENUM_NAME)
                        .values(List.of(
                                EnumValueDefinition.builder().value("RED").build(),
                                EnumValueDefinition.builder()
                                        .value("INJECTED = 1 #")
                                        .build()))
                        .build())))
                .build();

        assertThatThrownBy(() -> generator.write(definition, new InMemoryPythonFileWriter()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void acceptsValidEnum() {
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(List.of(TypeDefinition.enum_(EnumDefinition.builder()
                        .typeName(ENUM_NAME)
                        .values(List.of(
                                EnumValueDefinition.builder().value("RED").build(),
                                EnumValueDefinition.builder().value("GREEN").build(),
                                EnumValueDefinition.builder().value("BLUE").build()))
                        .build())))
                .build();

        InMemoryPythonFileWriter writer = new InMemoryPythonFileWriter();
        generator.write(definition, writer);
        assertThat(writer.getPythonFiles()).isNotEmpty();
    }
}
