/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python;

import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import java.util.Map;

public interface PythonFileGenerator<T> {

    PythonFile generateFile(
            Map<TypeName, TypeDefinition> types,
            PackageNameProcessor packageNameProvider,
            T definition);
}
