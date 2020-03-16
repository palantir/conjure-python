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

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Splitter;
import com.palantir.conjure.python.poet.PythonFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public interface PythonFileWriter {

    void writePythonFile(PythonFile file);

    static Path getPath(PythonFile file) {
        List<String> components =
                Splitter.on(".").splitToList(file.pythonPackage().get());
        checkState(!components.isEmpty());
        Path packageDir = Paths.get(
                components.get(0), components.subList(1, components.size()).toArray(new String[0]));
        return packageDir.resolve(file.fileName());
    }
}
