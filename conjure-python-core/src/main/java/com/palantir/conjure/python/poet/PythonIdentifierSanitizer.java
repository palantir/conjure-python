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

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Set;

public final class PythonIdentifierSanitizer {

    private static final ImmutableSet<String> pythonKeywords = ImmutableSet.of(
            "and",
            "as",
            "assert",
            "bool",
            "break",
            "class",
            "continue",
            "def",
            "del",
            "elif",
            "else",
            "except",
            "exec",
            "finally",
            "for",
            "from",
            "global",
            "if",
            "import",
            "in",
            "int",
            "is",
            "lambda",
            "not",
            "or",
            "pass",
            "print",
            "raise",
            "return",
            "str",
            "try",
            "while",
            "with",
            "yield");

    /**
     * If the identifier is a python keyword, prepends "_".
     *
     * <p>Does no case conversion.
     */
    public static String sanitize(String identifier) {
        return sanitize(identifier, Collections.emptySet());
    }

    public static String sanitize(String identifier, Set<String> protectedWords) {
        return isKeyword(identifier) || protectedWords.contains(identifier) ? identifier + "_" : identifier;
    }

    public static boolean isKeyword(String identifier) {
        return pythonKeywords.contains(identifier);
    }

    private PythonIdentifierSanitizer() {}
}
