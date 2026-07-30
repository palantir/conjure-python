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

package com.palantir.conjure.python.processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

public final class PythonIdentifierSanitizerTest {

    @Test
    public void acceptsOrdinaryIdentifiers() {
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("foo")).isTrue();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("_foo")).isTrue();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("Foo123")).isTrue();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("SNAKE_CASE_1")).isTrue();
    }

    @Test
    public void acceptsKeywordMangledNames() {
        assertThat(PythonIdentifierSanitizer.isValidIdentifier(PythonIdentifierSanitizer.sanitize("import")))
                .isTrue();
    }

    @Test
    public void rejectsNonIdentifiers() {
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("")).isFalse();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("1foo")).isFalse();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("has space")).isFalse();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("a-b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier("a.b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isValidIdentifier(null)).isFalse();
    }

    @Test
    public void checkValidIdentifierThrowsOnBadInput() {
        assertThatThrownBy(() -> PythonIdentifierSanitizer.checkValidIdentifier("has space"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void checkValidIdentifierReturnsInputOnGoodInput() {
        assertThat(PythonIdentifierSanitizer.checkValidIdentifier("foo")).isEqualTo("foo");
    }

    @Test
    public void rejectsValuesThatEscapeAStringLiteral() {
        // A quote or backslash closes/opens an escape; control chars (newline, carriage return, tab, NUL) end the line.
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("a'b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("a\"b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("a\\b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("a\nb")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("a\rb")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("a\tb")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral(null)).isFalse();
    }

    @Test
    public void allowsPathsPackagesAndParamIds() {
        // Values that legitimately reach a string-literal sink: HTTP paths (with '/' and '{}'), dotted package names,
        // kebab/camel parameter ids, and ordinary text with spaces. A space is not a control character, so it is safe.
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("/registry/{id}/versions/{version}"))
                .isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("com.example.product")).isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("Upper-Kebab-Header")).isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("has space")).isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeStringLiteral("GET")).isTrue();
    }

    @Test
    public void checkSafeStringLiteralThrowsOnBadInput() {
        assertThatThrownBy(() -> PythonIdentifierSanitizer.checkSafeStringLiteral("a'b"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void checkSafeStringLiteralReturnsInputOnGoodInput() {
        assertThat(PythonIdentifierSanitizer.checkSafeStringLiteral("/foo/{bar}")).isEqualTo("/foo/{bar}");
    }

    @Test
    public void allowsGenericTypeExpressions() {
        // Everything PythonTypeNameVisitor can compose has to keep passing.
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("str")).isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("List[int]")).isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("Dict[str, List[int]]")).isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("OptionalTypeWrapper[BinaryType]"))
                .isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("com.example.product_Widget"))
                .isTrue();
    }

    @Test
    public void rejectsTypeExpressionsThatCouldRunCode() {
        // No parentheses means no call can be expressed; no '=' or ';' means no assignment or extra statement.
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("Bad(Name)")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("a = b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("a; b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("a\nb")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression(null)).isFalse();
        // A bare type expression, unlike an annotation, is never quoted.
        assertThat(PythonIdentifierSanitizer.isSafeTypeExpression("\"Foo\"")).isFalse();
    }

    @Test
    public void allowsMyPyForwardReferences() {
        // MyPyTypeNameVisitor quotes a resolved reference, so annotations must permit double quotes.
        assertThat(PythonIdentifierSanitizer.isSafeTypeAnnotation("\"MyObject\"")).isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeTypeAnnotation("Optional[\"MyObject\"]"))
                .isTrue();
        assertThat(PythonIdentifierSanitizer.isSafeTypeAnnotation("Dict[str, \"Foo\"]")).isTrue();
    }

    @Test
    public void rejectsAnnotationsThatCouldRunCode() {
        assertThat(PythonIdentifierSanitizer.isSafeTypeAnnotation("Bad(Name)")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeAnnotation("a = b")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeAnnotation("a\nb")).isFalse();
        assertThat(PythonIdentifierSanitizer.isSafeTypeAnnotation(null)).isFalse();
    }

    @Test
    public void checkSafeTypeExpressionThrowsOnBadInput() {
        assertThatThrownBy(() -> PythonIdentifierSanitizer.checkSafeTypeExpression("Bad(Name)"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void checkSafeTypeExpressionReturnsInputOnGoodInput() {
        assertThat(PythonIdentifierSanitizer.checkSafeTypeExpression("Dict[str, int]")).isEqualTo("Dict[str, int]");
    }

    @Test
    public void checkSafeTypeAnnotationThrowsOnBadInput() {
        assertThatThrownBy(() -> PythonIdentifierSanitizer.checkSafeTypeAnnotation("Bad(Name)"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
