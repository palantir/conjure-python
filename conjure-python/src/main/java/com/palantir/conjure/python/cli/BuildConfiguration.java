/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.palantir.tokens.auth.ImmutablesStyle;
import java.io.IOException;
import org.immutables.value.Value;

@Value.Immutable
@ImmutablesStyle
@JsonDeserialize(as = ImmutableBuildConfiguration.class)
public abstract class BuildConfiguration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    abstract String generatorVersion();

    abstract String minConjureClientVersion();

    static BuildConfiguration load() {
        try {
            return OBJECT_MAPPER.readValue(
                    BuildConfiguration.class.getResourceAsStream("/buildConfiguration.yml"),
                    BuildConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
