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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

public final class PythonIdentifierSanitizer {

    // A valid Python identifier: ASCII letter or underscore, then letters/digits/underscores.
    // Intentionally stricter than CPython's Unicode-permitting str.isidentifier(); Conjure names are ASCII.
    private static final Pattern VALID_IDENTIFIER = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*$");

    // Characters that would let an IR-supplied value break out of a single- or double-quoted Python string literal:
    // a quote, a backslash (starts an escape), or any ASCII control character (includes newline, carriage return,
    // tab, NUL and DEL). Everything else -- including '/', '{', '}', '.', '-', ':', '~', spaces, and non-ASCII -- is
    // allowed, so legitimate HTTP paths, dotted package names, and parameter ids still pass.
    private static final Pattern FORBIDDEN_IN_STRING_LITERAL = Pattern.compile("[\\p{Cntrl}'\"\\\\]");

    // Some IR-derived values are emitted as bare Python code rather than inside a string literal: an alias target
    // ("Alias = <expr>"), the type passed to ConjureFieldDefinition, and mypy annotations. Quoting them is not an
    // option -- they must remain evaluable expressions -- so instead they are restricted to the characters the type
    // visitors can legitimately produce: identifiers, dots (dotted packages), and the subscript/tuple punctuation of
    // generics such as "Dict[str, List[int]]". Notably this excludes '(' and ')', so no call can be expressed, along
    // with '=', ';', quotes, backslash and control characters, so no assignment or statement break is possible.
    private static final Pattern VALID_TYPE_EXPRESSION = Pattern.compile("[A-Za-z0-9_., \\[\\]]+");

    // As VALID_TYPE_EXPRESSION, but also permits the double quotes that MyPyTypeNameVisitor puts around a forward
    // reference, for example Optional["MyObject"]. Annotations are never emitted inside a string literal, so a quote
    // here cannot escape one.
    private static final Pattern VALID_TYPE_ANNOTATION = Pattern.compile("[A-Za-z0-9_., \\[\\]\"]+");

    // Includes python keywords https://docs.python.org/3/reference/lexical_analysis.html#keywords.
    private static final ImmutableSet<String> pythonKeywords = ImmutableSet.of(
            "False",
            "None",
            "True",
            "and",
            "as",
            "assert",
            "async",
            "await",
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
            "nonlocal",
            "not",
            "or",
            "pass",
            "print",
            "raise",
            "return",
            "self",
            "str",
            "try",
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

    public static boolean isValidIdentifier(String identifier) {
        return identifier != null && VALID_IDENTIFIER.matcher(identifier).matches();
    }

    /**
     * Throws {@link IllegalArgumentException} if {@code identifier} is not a valid Python identifier. Returns the
     * identifier unchanged when valid, so it can be used inline.
     */
    public static String checkValidIdentifier(String identifier) {
        Preconditions.checkArgument(isValidIdentifier(identifier), "Not a valid Python identifier: %s", identifier);
        return identifier;
    }

    /**
     * Returns {@code true} if {@code value} can be safely emitted inside a single- or double-quoted Python string
     * literal -- that is, it contains no quote, backslash, or ASCII control character that could terminate or escape
     * the literal. Unlike {@link #isValidIdentifier}, this permits characters that are legitimate inside HTTP paths,
     * dotted package names, and parameter ids (for example {@code / . - : ~}, braces, and spaces).
     */
    public static boolean isSafeStringLiteral(String value) {
        return value != null && !FORBIDDEN_IN_STRING_LITERAL.matcher(value).find();
    }

    /**
     * Throws {@link IllegalArgumentException} if {@code value} is not safe to emit inside a Python string literal.
     * Returns the value unchanged when safe, so it can be used inline.
     */
    public static String checkSafeStringLiteral(String value) {
        Preconditions.checkArgument(
                isSafeStringLiteral(value),
                "Value is not safe to emit inside a Python string literal "
                        + "(contains a quote, backslash, or control character): %s",
                value);
        return value;
    }

    /**
     * Returns {@code true} if {@code value} can be safely emitted as a bare Python type expression -- an alias target
     * or the type handed to {@code ConjureFieldDefinition}. Unlike {@link #isValidIdentifier} this permits the
     * punctuation of a generic such as {@code Dict[str, List[int]]}; unlike {@link #isSafeStringLiteral} it forbids
     * parentheses, so the value cannot express a call.
     */
    public static boolean isSafeTypeExpression(String value) {
        return value != null && VALID_TYPE_EXPRESSION.matcher(value).matches();
    }

    /**
     * Throws {@link IllegalArgumentException} if {@code value} is not safe to emit as a bare Python type expression.
     * Returns the value unchanged when safe, so it can be used inline.
     */
    public static String checkSafeTypeExpression(String value) {
        Preconditions.checkArgument(
                isSafeTypeExpression(value),
                "Value is not safe to emit as a Python type expression "
                        + "(may only contain identifiers, dots, commas, spaces and square brackets): %s",
                value);
        return value;
    }

    /**
     * As {@link #isSafeTypeExpression}, but additionally permits the double quotes that surround a mypy forward
     * reference, for example {@code Optional["MyObject"]}.
     */
    public static boolean isSafeTypeAnnotation(String value) {
        return value != null && VALID_TYPE_ANNOTATION.matcher(value).matches();
    }

    /**
     * Throws {@link IllegalArgumentException} if {@code value} is not safe to emit as a mypy type annotation. Returns
     * the value unchanged when safe, so it can be used inline.
     */
    public static String checkSafeTypeAnnotation(String value) {
        Preconditions.checkArgument(
                isSafeTypeAnnotation(value),
                "Value is not safe to emit as a mypy type annotation (may only contain identifiers, dots, commas, "
                        + "spaces, square brackets and double quotes): %s",
                value);
        return value;
    }

    private PythonIdentifierSanitizer() {}
}
