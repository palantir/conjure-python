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

import static com.google.common.base.Preconditions.checkState;

import com.google.errorprone.annotations.FormatMethod;
import com.palantir.conjure.spec.Documentation;
import java.io.PrintStream;

public final class PythonPoetWriter {

    private static final String INDENT = "    ";

    private int indent;
    private PrintStream printStream;

    public PythonPoetWriter(PrintStream printStream) {
        this.indent = 0;
        this.printStream = printStream;
    }

    /**
     * Asserts that the code in runnable leaves the indent unchanged.
     */
    public PythonPoetWriter maintainingIndent(Runnable runnable) {
        int startIndent = indent;
        runnable.run();
        checkState(indent == startIndent, "expected indent to be unchanged");
        return this;
    }

    public PythonPoetWriter decreaseIndent() {
        this.indent--;
        return this;
    }

    public PythonPoetWriter increaseIndent() {
        this.indent++;
        return this;
    }

    public PythonPoetWriter write(String content) {
        printStream.print(content);
        return this;
    }

    public PythonPoetWriter writeLine() {
        printStream.print("\n");
        return this;
    }

    public PythonPoetWriter writeLine(String content) {
        printStream.print(content);
        return writeLine();
    }

    public PythonPoetWriter writeIndented() {
        for (int i = 0; i < indent; i++) {
            printStream.print(INDENT);
        }
        return this;
    }

    public PythonPoetWriter writeIndented(String content) {
        writeIndented();
        write(content);
        return this;
    }

    public PythonPoetWriter writeIndentedLine(String content) {
        writeIndented(content);
        writeLine();
        return this;
    }

    @FormatMethod
    public PythonPoetWriter writeIndentedLine(String formatString, Object... args) {
        return writeIndentedLine(String.format(formatString, args));
    }

    public PythonPoetWriter emit(Emittable emittable) {
        emittable.emit(this);
        return this;
    }

    public PythonPoetWriter writeDocs(Documentation docs) {
        writeIndentedLine("\"\"\"");
        writeIndentedLine(docs.get().trim());
        writeIndentedLine("\"\"\"");
        return this;
    }
}
