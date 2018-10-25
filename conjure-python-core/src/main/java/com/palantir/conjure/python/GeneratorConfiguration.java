/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python;

import com.palantir.tokens.auth.ImmutablesStyle;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@ImmutablesStyle
public interface GeneratorConfiguration {

    Optional<String> packageName();

    Optional<String> packageVersion();

    Optional<String> packageDescription();

    Optional<String> packageUrl();

    Optional<String> packageAuthor();

    String generatorVersion();

    String minConjureClientVersion();

    boolean shouldWriteCondaRecipe();

    boolean generateRawSource();

    @Value.Default
    default String maxConjureClientVersion() {
        return String.valueOf(Integer.parseInt(minConjureClientVersion().split("[.]")[0]) + 1);
    }

    final class Builder extends ImmutableGeneratorConfiguration.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
