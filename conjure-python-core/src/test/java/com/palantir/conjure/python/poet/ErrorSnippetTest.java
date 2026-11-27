/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public final class ErrorSnippetTest {
    @Test
    public void sanitizesProtectedFields() {
        ImmutableList<String> protectedFields =
                ImmutableList.of("add_note", "args", "decode", "encode", "error_instance_id", "with_traceback");

        ErrorSnippet.Builder snippet = ErrorSnippet.builder()
                .className("TestError")
                .definitionName("TestError")
                .definitionPackage(PythonPackage.of("test"))
                .pythonPackage(PythonPackage.of("test"))
                .errorCode("INVALID_ARGUMENT")
                .namespace("Test");
        protectedFields.forEach(field -> snippet.addSafeArgs(PythonField.builder()
                .attributeName(field)
                .jsonIdentifier(field)
                .pythonType("str")
                .myPyType("str")
                .isOptional(false)
                .build()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        snippet.build().emit(new PythonPoetWriter(new PrintStream(output)));
        String generated = output.toString(StandardCharsets.UTF_8);

        protectedFields.forEach(field -> {
            assertThat(generated).contains(field + "_: str");
            assertThat(generated).contains("self._" + field + "_ = " + field + "_");
            assertThat(generated).contains("def " + field + "_(self) -> str:");
            assertThat(generated).contains("ConjureEncoder.do_encode(self." + field + "_)");
            assertThat(generated).contains(field + "_=decoder.decode(");
        });
    }
}
