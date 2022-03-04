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

package com.palantir.conjure.python.poet;

import com.palantir.conjure.spec.Documentation;
import java.util.Comparator;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface PythonField {
    String attributeName();

    String jsonIdentifier();

    /**
     * The python type (or a conjure fake type) for this type.
     */
    String pythonType();

    /**
     * The mypy type for this type.
     */
    String myPyType();

    boolean isOptional();

    Optional<Documentation> docs();

    final class PythonFieldComparator implements Comparator<PythonField> {
        @Override
        public int compare(PythonField o1, PythonField o2) {
            if (o1.isOptional() && !o2.isOptional()) {
                return 1;
            }
            if (!o1.isOptional() && o2.isOptional()) {
                return -1;
            }
            return o1.attributeName().compareTo(o2.attributeName());
        }
    }

    class Builder extends ImmutablePythonField.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
