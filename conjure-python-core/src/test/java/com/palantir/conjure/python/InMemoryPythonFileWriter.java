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

import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonPoetWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class InMemoryPythonFileWriter implements PythonFileWriter {

    private final Map<Path, String> pythonFiles = new HashMap<>();

    public Map<Path, String> getPythonFiles() {
        return pythonFiles;
    }

    @Override
    public void writePythonFile(PythonFile pythonFile) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(baos)) {
            PythonPoetWriter poetWriter = new PythonPoetWriter(printStream);

            poetWriter.emit(pythonFile);
            byte[] bytes = baos.toByteArray();

            pythonFiles.put(PythonFileWriter.getPath(pythonFile), new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
