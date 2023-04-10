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

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.python.processors.packagename.CompoundPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.FlatteningPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.PackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.PrefixingPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.TwoComponentStrippingPackageNameProcessor;
import org.junit.jupiter.api.Test;

public final class PackageNameProcessorTests {

    @Test
    public void testPackageNameProcessor() {
        PackageNameProcessor processor = new CompoundPackageNameProcessor(ImmutableList.of());
        assertThat(processor.process("com.palantir.test")).isEqualTo("com.palantir.test");
    }

    @Test
    public void testTwoComponentStrippingPackageNameProcessor() {
        PackageNameProcessor processor =
                new CompoundPackageNameProcessor(ImmutableList.of(new TwoComponentStrippingPackageNameProcessor()));
        assertThat(processor.process("com.palantir.test")).isEqualTo("test");
        assertThat(processor.process("com.palantir")).isEqualTo("com.palantir");
    }

    @Test
    public void testTopLevelRenamingPackageNameProcessor() {
        PackageNameProcessor processor =
                new CompoundPackageNameProcessor(ImmutableList.of(new PrefixingPackageNameProcessor("toplevel")));
        assertThat(processor.process("test")).isEqualTo("toplevel.test");
        assertThat(processor.process("test.whatever")).isEqualTo("toplevel.test.whatever");
    }

    @Test
    public void testFlatteningPackageNameProcessor() {
        PackageNameProcessor processor =
                new CompoundPackageNameProcessor(ImmutableList.of(FlatteningPackageNameProcessor.INSTANCE));
        assertThat(processor.process("data.test.api")).isEqualTo("data_test_api");
    }

    @Test
    public void testPackageNameProcessorComposition() {
        PackageNameProcessor processor = new CompoundPackageNameProcessor(ImmutableList.of(
                new TwoComponentStrippingPackageNameProcessor(),
                FlatteningPackageNameProcessor.INSTANCE,
                new PrefixingPackageNameProcessor("toplevel")));
        assertThat(processor.process("test")).isEqualTo("toplevel.test");
        assertThat(processor.process("test.whatever")).isEqualTo("toplevel.test_whatever");
        assertThat(processor.process("com.palantir.test")).isEqualTo("toplevel.test");
        assertThat(processor.process("com.palantir.test.whatever")).isEqualTo("toplevel.test_whatever");
    }
}
