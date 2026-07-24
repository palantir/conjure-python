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
}
