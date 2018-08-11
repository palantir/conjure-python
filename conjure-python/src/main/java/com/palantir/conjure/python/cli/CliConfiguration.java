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

import com.google.common.base.Preconditions;
import com.palantir.tokens.auth.ImmutablesStyle;
import java.io.File;
import java.util.Optional;
import org.apache.commons.cli.Option;
import org.immutables.value.Value;

@Value.Immutable
@ImmutablesStyle
public abstract class CliConfiguration {
    static final String RAW_SOURCE = "rawSource";
    static final String PACKAGE_NAME = "packageName";
    static final String PACKAGE_VERSION = "packageVersion";
    static final String PACKAGE_DESCRIPTION = "packageDescription";
    static final String PACKAGE_URL = "packageUrl";
    static final String PACKAGE_AUTHOR = "packageAuthor";
    static final String WRITE_CONDA_RECIPE = "writeCondaRecipe";

    abstract File target();

    abstract File outputDirectory();

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
        Preconditions.checkArgument(target().isFile(), "Target must exist and be a file");
        Preconditions.checkArgument(outputDirectory().isDirectory(), "Output must exist and be a directory");
    }

    static CliConfiguration of(String target, String outputDirectory, Option[] options) {
        Builder builder = new Builder()
                .target(new File(target))
                .outputDirectory(new File(outputDirectory));
        for (Option option : options) {
            switch (option.getLongOpt()) {
                case PACKAGE_NAME:
                    builder.packageName(option.getValue());
                    break;
                case PACKAGE_VERSION:
                    String pythonicVersion = option.getValue().replace('-', '_');
                    builder.packageVersion(pythonicVersion);
                    break;
                case PACKAGE_DESCRIPTION:
                    builder.packageDescription(option.getValue());
                    break;
                case PACKAGE_URL:
                    builder.packageUrl(option.getValue());
                    break;
                case PACKAGE_AUTHOR:
                    builder.packageAuthor(option.getValue());
                    break;
                case WRITE_CONDA_RECIPE:
                    builder.shouldWriteCondaRecipe(true);
                    break;
                case RAW_SOURCE:
                    builder.generateRawSource(true);
                    break;
                default:
                    break;
            }
        }
        return builder.build();
    }

    static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ImmutableCliConfiguration.Builder {}
}
