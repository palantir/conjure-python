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

package com.palantir.conjure.python.processors.packagename;

import java.util.List;

/**
 * Post process package names.
 */
public final class CompoundPackageNameProcessor implements PackageNameProcessor {

    private final List<PackageNameProcessor> processors;

    public CompoundPackageNameProcessor(List<PackageNameProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public String process(String packageName) {
        String updatedPackage = packageName;
        for (PackageNameProcessor processor : processors) {
            updatedPackage = processor.process(updatedPackage);
        }
        return updatedPackage;
    }
}
