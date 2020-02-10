/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python;

public final class FlatteningPackageNameProcessor implements PackageNameProcessor.Processor {

    @Override
    public String processPackageName(String packageName) {
        return packageName.replace(".", "_");
    }
}
