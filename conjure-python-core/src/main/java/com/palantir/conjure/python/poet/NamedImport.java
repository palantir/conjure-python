/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
public interface NamedImport {

    @Value.Parameter
    String name();

    @Value.Parameter
    Optional<String> alias();

    static NamedImport of(String name) {
        return ImmutableNamedImport.of(name, Optional.empty());
    }

    static NamedImport of(String name, String alias) {
        return ImmutableNamedImport.of(name, Optional.of(alias));
    }

    default String render() {
        if (alias().isPresent()) {
            return String.format("%s as %s", name(), alias().get());
        }
        return String.format("%s", name());
    }
}
