/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python.poet;

import org.immutables.value.Value;

@Value.Immutable
public interface PythonLine extends PythonClass {
    String text();

    @Override
    default String className() {
        return "__version__";
    }

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(text());
        poetWriter.writeLine();
    }

    class Builder extends ImmutablePythonLine.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
