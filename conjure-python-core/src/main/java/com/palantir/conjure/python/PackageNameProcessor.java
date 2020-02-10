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

import com.palantir.tokens.auth.ImmutablesStyle;
import java.util.List;
import org.immutables.value.Value;

/**
 * Post process package names.
 */
@Value.Immutable
@ImmutablesStyle
public abstract class PackageNameProcessor {
    PackageNameProcessor() {}

    abstract List<Processor> processors();

    public final String getPackageName(String packageName) {
        String updatedPackage = packageName;
        for (Processor processor : processors()) {
            updatedPackage = processor.processPackageName(updatedPackage);
        }
        return updatedPackage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends ImmutablePackageNameProcessor.Builder {}

    public interface Processor {
        String processPackageName(String packageName);
    }
}
