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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DefaultPythonFileWriter implements PythonFileWriter {

    private final Path basePath;

    public DefaultPythonFileWriter(Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public void writePythonFile(PythonFile file) {
        Path filePath = basePath.resolve(PythonFileWriter.getPath(file));
        try {
            Files.createDirectories(filePath.getParent());
            try (OutputStream os = Files.newOutputStream(filePath);
                    PrintStream ps = new PrintStream(os)) {
                PythonPoetWriter writer = new PythonPoetWriter(ps);
                writer.emit(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
