/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python.poet;

import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.tokens.auth.ImmutablesStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutablesStyle
public interface AliasSnippet extends PythonSnippet {

    @Override
    @Value.Default
    default String idForSorting() {
        return className();
    }

    String className();

    String aliasName();

    AliasDefinition aliasType();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.writeIndentedLine(String.format("%s = %s", className(), aliasName()));
        poetWriter.writeLine();
    }

    class Builder extends ImmutableAliasSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
