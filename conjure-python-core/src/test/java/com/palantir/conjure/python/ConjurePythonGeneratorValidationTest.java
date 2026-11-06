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

import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ArgumentDefinition;
import com.palantir.conjure.spec.ArgumentName;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.conjure.spec.EndpointName;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.EnumValueDefinition;
import com.palantir.conjure.spec.HttpMethod;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PathParameterType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import java.util.List;
import org.junit.Test;

/**
 * Verifies that the generator rejects IR-supplied identifiers that would not be valid Python identifiers, before
 * emitting any source. Definitions are built directly via the spec builders (not {@code Conjure.parse}), matching how
 * a deserialized definition reaches the generator.
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
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(List.of(TypeDefinition.enum_(EnumDefinition.builder()
                        .typeName(ENUM_NAME)
                        .values(List.of(
                                EnumValueDefinition.builder().value("RED").build(),
                                EnumValueDefinition.builder().value("GREEN = 1").build()))
                        .build())))
                .build();

        assertThatThrownBy(() -> generator.write(definition, new InMemoryPythonFileWriter()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void rejectsTypeNameThatIsNotAValidIdentifier() {
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(List.of(TypeDefinition.enum_(EnumDefinition.builder()
                        .typeName(TypeName.of("Bad Name", "com.example.product"))
                        .values(List.of(
                                EnumValueDefinition.builder().value("RED").build()))
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

    @Test
    public void rejectsAliasTargetThatCouldRunCode() {
        // The alias target is a type this definition never declares, in the alias's own package. No snippet is
        // generated for it, and no import is emitted because the package matches, so the alias snippet's own check is
        // the only thing standing between the name and being emitted as bare code.
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(List.of(TypeDefinition.alias(AliasDefinition.builder()
                        .typeName(TypeName.of("Widget", "com.example.product"))
                        .alias(Type.reference(TypeName.of("Bad(Name)", "com.example.product")))
                        .build())))
                .build();

        assertThatThrownBy(() -> generator.write(definition, new InMemoryPythonFileWriter()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not safe to emit as a Python type expression");
    }

    @Test
    public void acceptsAliasOfPrimitive() {
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .types(List.of(TypeDefinition.alias(AliasDefinition.builder()
                        .typeName(TypeName.of("Widget", "com.example.product"))
                        .alias(Type.primitive(PrimitiveType.STRING))
                        .build())))
                .build();

        InMemoryPythonFileWriter writer = new InMemoryPythonFileWriter();
        generator.write(definition, writer);
        assertThat(writer.getPythonFiles()).isNotEmpty();
    }

    @Test
    public void rejectsHttpPathThatEscapesStringLiteral() {
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .services(List.of(ServiceDefinition.builder()
                        .serviceName(TypeName.of("WidgetService", "com.example.product"))
                        .endpoints(EndpointDefinition.builder()
                                .endpointName(EndpointName.of("getWidget"))
                                .httpMethod(HttpMethod.GET)
                                .httpPath(HttpPath.of("/x'y"))
                                .build())
                        .build()))
                .build();

        assertThatThrownBy(() -> generator.write(definition, new InMemoryPythonFileWriter()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not safe to emit inside a Python string literal");
    }

    @Test
    public void acceptsServiceWithTemplatedPath() {
        ConjureDefinition definition = ConjureDefinition.builder()
                .version(1)
                .services(List.of(ServiceDefinition.builder()
                        .serviceName(TypeName.of("WidgetService", "com.example.product"))
                        .endpoints(EndpointDefinition.builder()
                                .endpointName(EndpointName.of("getWidget"))
                                .httpMethod(HttpMethod.GET)
                                .httpPath(HttpPath.of("/widgets/{widgetId}"))
                                .args(ArgumentDefinition.builder()
                                        .argName(ArgumentName.of("widgetId"))
                                        .type(Type.primitive(PrimitiveType.STRING))
                                        .paramType(ParameterType.path(PathParameterType.of()))
                                        .build())
                                .build())
                        .build()))
                .build();

        InMemoryPythonFileWriter writer = new InMemoryPythonFileWriter();
        generator.write(definition, writer);
        assertThat(writer.getPythonFiles()).isNotEmpty();
    }
}
