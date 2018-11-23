/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python.poet;

import java.util.Set;

public interface PythonSnippet extends Emittable {
    /* The set of imports required for this snippet to compile */
    Set<PythonImport> imports();

    /* The name of the snippet */
    String name();
}
