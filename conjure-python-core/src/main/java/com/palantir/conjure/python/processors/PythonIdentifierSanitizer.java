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

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Set;

public final class PythonIdentifierSanitizer {

    // Includes python keywords https://docs.python.org/3/reference/lexical_analysis.html#keywords,
    // soft keywords https://docs.python.org/3/reference/lexical_analysis.html#soft-keywords,
    // and built-in types https://docs.python.org/3/library/stdtypes.html#.
    private static final ImmutableSet<String> pythonKeywords = ImmutableSet.of(
            "and",
            "as",
            "assert",
            "async",
            "await",
            "bool",
            "break",
            "bytes",
            "bytearray",
            "case",
            "class",
            "complex",
            "continue",
            "def",
            "del",
            "dict",
            "elif",
            "else",
            "except",
            "exec",
            "finally",
            "float",
            "for",
            "from",
            "frozenset",
            "global",
            "if",
            "import",
            "in",
            "int",
            "is",
            "lambda",
            "list",
            "match",
            "memoryview",
            "nonlocal",
            "not",
            "or",
            "pass",
            "print",
            "raise",
            "range",
            "return",
            "set",
            "str",
            "try",
            "tuple",
            "type",
            "while",
            "with",
            "yield");

    /**
     * If the identifier is a python keyword, prepends "_".
     * <p>
     * Does no case conversion.
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
