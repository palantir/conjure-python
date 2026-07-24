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

package com.palantir.conjure.python.poet;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.palantir.conjure.python.poet.EnumSnippet.PythonEnumValue;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.HttpMethod;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import java.util.Optional;
import org.junit.Test;

public final class PoetIdentifierValidationTest {

    private static final PythonPackage PACKAGE = PythonPackage.of("test_package");

    @Test
    public void pythonFieldRejectsInvalidAttributeName() {
        assertThatThrownBy(() -> PythonField.builder()
                        .attributeName("has space")
                        .jsonIdentifier("hasSpace")
                        .pythonType("str")
                        .myPyType("str")
                        .isOptional(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void enumSnippetRejectsInvalidEnumValue() {
        assertThatThrownBy(() -> EnumSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Color")
                        .definitionName("Color")
                        .definitionPackage(PACKAGE)
                        .values(java.util.List.of(PythonEnumValue.of("RED = 1", Optional.empty())))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void enumSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> EnumSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Color")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void beanSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> BeanSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Bean")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void unionSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> UnionSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Union")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonServiceRejectsInvalidClassName() {
        assertThatThrownBy(() -> PythonService.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Service")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void aliasSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> AliasSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .aliasName("str")
                        .aliasType(AliasDefinition.builder()
                                .typeName(TypeName.of("Alias", "com.example.product"))
                                .alias(Type.primitive(PrimitiveType.STRING))
                                .build())
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonEndpointRejectsInvalidMethodName() {
        assertThatThrownBy(() -> PythonEndpointDefinition.builder()
                        .pythonMethodName("bad name")
                        .httpMethod(HttpMethod.GET)
                        .httpPath(HttpPath.of("/foo"))
                        .isRequestBinary(false)
                        .isResponseBinary(false)
                        .isOptionalReturnType(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
