/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonLine;
import com.palantir.conjure.python.poet.PythonPackage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DefaultPythonFileWriterTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    private final PythonPackage foo = PythonPackage.of("foo");
    private final PythonFile file = PythonFile.builder()
            .pythonPackage(foo)
            .fileName("test.py")
            .addContents(
                    PythonLine.builder().pythonPackage(foo).text("# Emoji: 🌶").build())
            .build();

    @Test
    public void special_characters_turn_into_question_marks_for_python_2() {
        new DefaultPythonFileWriter(folder.getRoot().toPath(), 2).writePythonFile(file);

        assertThat(folder.getRoot().toPath().resolve("foo/test.py")).hasContent("# Emoji: ?\n\n");
    }

    @Test
    public void special_characters_preserved_for_python_3() {
        new DefaultPythonFileWriter(folder.getRoot().toPath(), 3).writePythonFile(file);

        assertThat(folder.getRoot().toPath().resolve("foo/test.py")).hasContent("# Emoji: 🌶\n\n");
    }
}
