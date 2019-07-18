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

package com.palantir.conjure.python.cli;

import com.palantir.tokens.auth.ImmutablesStyle;
import java.io.File;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@ImmutablesStyle
public abstract class CliConfiguration {
    abstract File input();

    abstract File output();

    abstract Optional<String> packageName();

    abstract Optional<String> packageVersion();

    abstract Optional<String> packageDescription();

    abstract Optional<String> packageUrl();

    abstract Optional<String> packageAuthor();

    @Value.Default
    @SuppressWarnings("DesignForExtension")
    boolean generateRawSource() {
        return false;
    }

    @Value.Default
    @SuppressWarnings("DesignForExtension")
    boolean shouldWriteCondaRecipe() {
        return false;
    }


    @Value.Check
    final void check() {
        com.palantir.logsafe.Preconditions.checkArgument(input().isFile(), "Target must exist and be a file");
        com.palantir.logsafe.Preconditions.checkArgument(output().isDirectory(), "Output must exist and be a directory");
    }

    static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ImmutableCliConfiguration.Builder {}
}
