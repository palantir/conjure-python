/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.python;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.python.processors.packagename.CompoundPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.FlatteningPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.PackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.TopLevelAddingPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.TwoComponentStrippingPackageNameProcessor;
import org.junit.Test;

public final class PackageNameProcessorTests {

    @Test
    public void testPackageNameProcessor() {
        PackageNameProcessor processor = CompoundPackageNameProcessor.builder().build();
        assertThat(processor.process("com.palantir.test")).isEqualTo("com.palantir.test");
    }

    @Test
    public void testTwoComponentStrippingPackageNameProcessor() {
        PackageNameProcessor processor = CompoundPackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .build();
        assertThat(processor.process("com.palantir.test")).isEqualTo("test");
        assertThat(processor.process("com.palantir")).isEqualTo("com.palantir");
    }

    @Test
    public void testTopLevelRenamingPackageNameProcessor() {
        PackageNameProcessor processor = CompoundPackageNameProcessor.builder()
                .addProcessors(new TopLevelAddingPackageNameProcessor("toplevel"))
                .build();
        assertThat(processor.process("test")).isEqualTo("toplevel.test");
        assertThat(processor.process("test.whatever")).isEqualTo("toplevel.test.whatever");
    }

    @Test
    public void testFlatteningPackageNameProcessor() {
        PackageNameProcessor processor = CompoundPackageNameProcessor.builder()
                .addProcessors(FlatteningPackageNameProcessor.INSTANCE)
                .build();
        assertThat(processor.process("data.test.api")).isEqualTo("data_test_api");
    }

    @Test
    public void testPackageNameProcessorComposition() {
        PackageNameProcessor processor = CompoundPackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .addProcessors(FlatteningPackageNameProcessor.INSTANCE)
                .addProcessors(new TopLevelAddingPackageNameProcessor("toplevel"))
                .build();
        assertThat(processor.process("test")).isEqualTo("toplevel.test");
        assertThat(processor.process("test.whatever")).isEqualTo("toplevel.test_whatever");
        assertThat(processor.process("com.palantir.test")).isEqualTo("toplevel.test");
        assertThat(processor.process("com.palantir.test.whatever")).isEqualTo("toplevel.test_whatever");
    }
}
