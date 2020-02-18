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

import org.junit.Test;

public final class PackageNameProcessorTests {

    @Test
    public void testPackageNameProcessor() {
        PackageNameProcessor processor = PackageNameProcessor.builder().build();
        assertThat(processor.getPackageName("com.palantir.test")).isEqualTo("com.palantir.test");
    }

    @Test
    public void testTwoComponentStrippingPackageNameProcessor() {
        PackageNameProcessor processor = PackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .build();
        assertThat(processor.getPackageName("com.palantir.test")).isEqualTo("test");
        assertThat(processor.getPackageName("com.palantir")).isEqualTo("com.palantir");
    }

    @Test
    public void testTopLevelRenamingPackageNameProcessor() {
        PackageNameProcessor processor = PackageNameProcessor.builder()
                .addProcessors(new TopLevelAddingPackageNameProcessor("toplevel"))
                .build();
        assertThat(processor.getPackageName("test")).isEqualTo("toplevel.test");
        assertThat(processor.getPackageName("test.whatever")).isEqualTo("toplevel.test.whatever");
    }

    @Test
    public void testFlatteningPackageNameProcessor() {
        PackageNameProcessor processor = PackageNameProcessor.builder()
                .addProcessors(new FlatteningPackageNameProcessor())
                .build();
        assertThat(processor.getPackageName("data.test.api")).isEqualTo("data_test_api");
    }

    @Test
    public void testPackageNameProcessorComposition() {
        PackageNameProcessor processor = PackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .addProcessors(new FlatteningPackageNameProcessor())
                .addProcessors(new TopLevelAddingPackageNameProcessor("toplevel"))
                .build();
        assertThat(processor.getPackageName("test")).isEqualTo("toplevel.test");
        assertThat(processor.getPackageName("test.whatever")).isEqualTo("toplevel.test_whatever");
        assertThat(processor.getPackageName("com.palantir.test")).isEqualTo("toplevel.test");
        assertThat(processor.getPackageName("com.palantir.test.whatever")).isEqualTo("toplevel.test_whatever");
    }
}
