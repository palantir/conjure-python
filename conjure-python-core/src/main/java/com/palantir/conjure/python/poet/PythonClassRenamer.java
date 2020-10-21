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

package com.palantir.conjure.python.poet;

/**
 * Utility class for renaming a class from its globally unique name that we use for writing
 * in the _impl package into the module-scoped name that it should be accessed by.
 */
public final class PythonClassRenamer {
    private PythonClassRenamer() {}

    static void renameClass(
            PythonPoetWriter poetWriter, String className, PythonPackage definitionPackage, String definitionName) {
        poetWriter.writeIndentedLine(String.format("%s.__name__ = \"%s\"", className, definitionName));
        poetWriter.writeIndentedLine(String.format("%s.__qualname__ = \"%s\"", className, definitionName));
        poetWriter.writeIndentedLine(String.format("%s.__module__ = \"%s\"", className, definitionPackage.get()));

        poetWriter.writeLine();
        poetWriter.writeLine();
    }
}
