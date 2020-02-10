/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python;

public final class TopLevelAddingPackageNameProcessor implements PackageNameProcessor.Processor {

    private String topLevelPackageName;

    TopLevelAddingPackageNameProcessor(String topLevelPackageName) {
        this.topLevelPackageName = topLevelPackageName;
    }

    @Override
    public String processPackageName(String packageName) {
        return topLevelPackageName + "." + packageName;
    }
}
