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

import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface AllSnippet extends PythonSnippet {
    @Override
    default String idForSorting() {
        return "__all__";
    }

    List<String> contents();

    @Override
    default void emit(PythonPoetWriter poetWriter) {
        poetWriter.maintainingIndent(() -> {
            poetWriter.writeIndentedLine("__all__ = [");
            poetWriter.increaseIndent();
            contents().forEach(a -> poetWriter.writeIndentedLine("'%s',", a));
            poetWriter.decreaseIndent();
            poetWriter.writeIndentedLine("]");
            poetWriter.writeLine();
        });
    }

    class Builder extends ImmutableAllSnippet.Builder {}

    static Builder builder() {
        return new Builder();
    }
}
