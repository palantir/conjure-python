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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.palantir.common.streams.KeyedStream;
import com.palantir.conjure.python.client.ClientGenerator;
import com.palantir.conjure.python.poet.AllSnippet;
import com.palantir.conjure.python.poet.EmptySnippet;
import com.palantir.conjure.python.poet.NamedImport;
import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonLine;
import com.palantir.conjure.python.poet.PythonMetaYaml;
import com.palantir.conjure.python.poet.PythonPackage;
import com.palantir.conjure.python.poet.PythonSetup;
import com.palantir.conjure.python.poet.PythonSnippet;
import com.palantir.conjure.python.processors.packagename.CompoundPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.ConstantPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.FlatteningPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.PackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.TopLevelAddingPackageNameProcessor;
import com.palantir.conjure.python.processors.packagename.TwoComponentStrippingPackageNameProcessor;
import com.palantir.conjure.python.processors.typename.NameOnlyTypeNameProcessor;
import com.palantir.conjure.python.processors.typename.PackagePrependingTypeNameProcessor;
import com.palantir.conjure.python.processors.typename.TypeNameProcessor;
import com.palantir.conjure.python.types.PythonTypeGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.TypeName;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ConjurePythonGenerator {

    private static final String INIT_PY = "__init__.py";
    private static final String IMPL_PY = "_impl.py";

    private final GeneratorConfiguration config;

    public ConjurePythonGenerator(GeneratorConfiguration config) {
        Preconditions.checkArgument(
                config.generateRawSource()
                        || (config.packageName().isPresent()
                                && config.packageVersion().isPresent()),
                "If generateRawSource is not set, packageName and packageVersion must be present");
        Preconditions.checkArgument(
                !(config.generateRawSource() && config.shouldWriteCondaRecipe()),
                "If generateRawSource is set, shouldWriteCondaRecipe must not be set");
        this.config = config;
    }

    public void write(ConjureDefinition conjureDefinition, PythonFileWriter writer) {
        generate(conjureDefinition).forEach(writer::writePythonFile);

        PythonPackage rootPackage = PythonPackage.of(buildPackageNameProcessor().process(""));
        if (!config.generateRawSource()) {
            writer.writePythonFile(buildPythonSetupFile(rootPackage));
        }
        if (config.shouldWriteCondaRecipe()) {
            writer.writePythonFile(buildCondaMetaYamlFile(rootPackage));
        }
    }

    private List<PythonFile> generate(ConjureDefinition conjureDefinition) {
        DealiasingTypeVisitor dealiasingTypeVisitor = new DealiasingTypeVisitor(conjureDefinition.getTypes().stream()
                .collect(Collectors.toMap(type -> type.accept(TypeDefinitionVisitor.TYPE_NAME), Function.identity())));

        PackageNameProcessor implPackageNameProcessor =
                new ConstantPackageNameProcessor(config.pythonicPackageName().orElse(""));

        PackageNameProcessor definitionPackageNameProcessor = CompoundPackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .addProcessors(FlatteningPackageNameProcessor.INSTANCE)
                .addAllProcessors(config.pythonicPackageName()
                        .map(pythonPackageName ->
                                ImmutableSet.of(new TopLevelAddingPackageNameProcessor(pythonPackageName)))
                        .orElseGet(ImmutableSet::of))
                .build();

        TypeNameProcessor implTypeNameProcessor =
                new PackagePrependingTypeNameProcessor(CompoundPackageNameProcessor.builder()
                        .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                        .addProcessors(FlatteningPackageNameProcessor.INSTANCE)
                        .build());

        TypeNameProcessor definitionTypeNameProcessor = NameOnlyTypeNameProcessor.INSTANCE;

        List<PythonFile> pythonFiles = new ArrayList<>();
        pythonFiles.add(getImplPythonFile(
                conjureDefinition,
                dealiasingTypeVisitor,
                implPackageNameProcessor,
                implTypeNameProcessor,
                definitionPackageNameProcessor,
                definitionTypeNameProcessor));
        List<PythonFile> initFiles = getInitFiles(
                conjureDefinition, implTypeNameProcessor, definitionPackageNameProcessor, definitionTypeNameProcessor);
        pythonFiles.addAll(initFiles);

        PythonPackage rootPackage = PythonPackage.of(buildPackageNameProcessor().process("."));

        pythonFiles.add(getRootInit(
                initFiles.stream().map(PythonFile::pythonPackage).collect(Collectors.toSet()), rootPackage));

        return pythonFiles;
    }

    private PythonFile getImplPythonFile(
            ConjureDefinition conjureDefinition,
            DealiasingTypeVisitor dealiasingTypeVisitor,
            PackageNameProcessor implPackageNameProcessor,
            TypeNameProcessor implTypeNameProcessor,
            PackageNameProcessor definitionPackageNameProcessor,
            TypeNameProcessor definitionTypeNameProcessor) {
        PythonTypeGenerator beanGenerator = new PythonTypeGenerator(
                implPackageNameProcessor,
                implTypeNameProcessor,
                definitionPackageNameProcessor,
                definitionTypeNameProcessor,
                dealiasingTypeVisitor);
        ClientGenerator clientGenerator = new ClientGenerator(
                implPackageNameProcessor,
                implTypeNameProcessor,
                definitionPackageNameProcessor,
                definitionTypeNameProcessor,
                dealiasingTypeVisitor);

        List<PythonSnippet> snippets = new ArrayList<>();
        snippets.addAll(conjureDefinition.getTypes().stream()
                .map(beanGenerator::generateType)
                .collect(Collectors.toList()));
        snippets.addAll(conjureDefinition.getServices().stream()
                .map(clientGenerator::generateClient)
                .collect(Collectors.toList()));

        Map<PythonPackage, List<PythonSnippet>> snippetsByPackage =
                snippets.stream().collect(Collectors.groupingBy(PythonSnippet::pythonPackage));

        PythonPackage rootPackage = PythonPackage.of(implPackageNameProcessor.process(""));
        List<PythonFile> pythonFiles = KeyedStream.stream(snippetsByPackage)
                .map((pythonPackage, pythonSnippets) -> PythonFile.builder()
                        .pythonPackage(rootPackage)
                        .fileName(IMPL_PY)
                        .contents(pythonSnippets)
                        .build())
                .values()
                .collect(Collectors.toList());

        return Iterables.getOnlyElement(pythonFiles);
    }

    private List<PythonFile> getInitFiles(
            ConjureDefinition conjureDefinition,
            TypeNameProcessor implTypeNameProcessor,
            PackageNameProcessor definitionPackageNameProcessor,
            TypeNameProcessor definitionTypeNameProcessor) {
        String moduleSpecifier = ".._impl";

        List<PythonSnippet> snippets = new ArrayList<>();
        snippets.addAll(conjureDefinition.getTypes().stream()
                .map(typeDefinition -> {
                    TypeName typeName = typeDefinition.accept(TypeDefinitionVisitor.TYPE_NAME);
                    return EmptySnippet.builder()
                            .pythonPackage(
                                    PythonPackage.of(definitionPackageNameProcessor.process(typeName.getPackage())))
                            .addImports(PythonImport.of(
                                    moduleSpecifier,
                                    NamedImport.of(
                                            implTypeNameProcessor.process(typeName),
                                            definitionTypeNameProcessor.process(typeName))))
                            .build();
                })
                .collect(Collectors.toList()));

        snippets.addAll(conjureDefinition.getServices().stream()
                .map(serviceDefinition -> EmptySnippet.builder()
                        .pythonPackage(PythonPackage.of(definitionPackageNameProcessor.process(
                                serviceDefinition.getServiceName().getPackage())))
                        .addImports(PythonImport.of(
                                moduleSpecifier,
                                NamedImport.of(
                                        implTypeNameProcessor.process(serviceDefinition.getServiceName()),
                                        definitionTypeNameProcessor.process(serviceDefinition.getServiceName()))))
                        .build())
                .collect(Collectors.toList()));

        Map<PythonPackage, List<PythonSnippet>> snippetsByPackage =
                snippets.stream().collect(Collectors.groupingBy(PythonSnippet::pythonPackage));

        return KeyedStream.stream(snippetsByPackage)
                .map((pythonPackage, pythonSnippets) -> PythonFile.builder()
                        .pythonPackage(pythonPackage)
                        .fileName(INIT_PY)
                        .contents(pythonSnippets)
                        .build())
                .values()
                .collect(Collectors.toList());
    }

    private PythonFile getRootInit(Set<PythonPackage> packageNames, PythonPackage rootPackage) {
        String rootInitFilePath = config.pythonicPackageName().orElse("");
        PythonFile.Builder builder = PythonFile.builder()
                .pythonPackage(PythonPackage.of(config.pythonicPackageName().orElse(".")))
                .fileName(INIT_PY)
                .addContents(AllSnippet.builder()
                        .pythonPackage(rootPackage)
                        .contents(packageNames.stream()
                                .map(pythonPackage -> pythonPackage
                                        .get()
                                        .replace(rootInitFilePath, "")
                                        .replace(".", ""))
                                .sorted()
                                .collect(Collectors.toList()))
                        .build())
                .addContents(PythonLine.builder()
                        .pythonPackage(rootPackage)
                        .text(String.format("__conjure_generator_version__ = \"%s\"", config.generatorVersion()))
                        .build());
        config.packageVersion()
                .ifPresent(version -> builder.addContents(PythonLine.builder()
                        .pythonPackage(rootPackage)
                        .text(String.format(
                                "__version__ = \"%s\"", config.packageVersion().get()))
                        .build()));
        return builder.build();
    }

    private PythonFile buildPythonSetupFile(PythonPackage rootPackage) {
        PythonSetup.Builder builder = PythonSetup.builder()
                .pythonPackage(rootPackage)
                .putOptions("name", config.packageName().get())
                .putOptions("version", config.packageVersion().get())
                .addInstallDependencies("requests", "typing")
                .addInstallDependencies(String.format(
                        "conjure-python-client>=%s,<%s",
                        config.minConjureClientVersion(), config.maxConjureClientVersion()))
                .addInstallDependencies("future");
        config.packageDescription().ifPresent(value -> builder.putOptions("description", value));
        config.packageUrl().ifPresent(value -> builder.putOptions("url", value));
        config.packageAuthor().ifPresent(value -> builder.putOptions("author", value));

        return PythonFile.builder()
                .pythonPackage(PythonPackage.of("."))
                .fileName("setup.py")
                .addContents(builder.build())
                .build();
    }

    private PythonFile buildCondaMetaYamlFile(PythonPackage rootPackage) {
        return PythonFile.builder()
                .pythonPackage(PythonPackage.of("conda_recipe"))
                .fileName("meta.yaml")
                .addContents(PythonMetaYaml.builder()
                        .pythonPackage(rootPackage)
                        .condaPackageName(config.packageName().get())
                        .packageVersion(config.packageVersion().get())
                        .addInstallDependencies("requests", "typing")
                        .addInstallDependencies(String.format(
                                "conjure-python-client >=%s,<%s",
                                config.minConjureClientVersion(), config.maxConjureClientVersion()))
                        .build())
                .build();
    }

    private PackageNameProcessor buildPackageNameProcessor() {
        CompoundPackageNameProcessor.Builder compoundPackageNameProcessor = CompoundPackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .addAllProcessors(config.pythonicPackageName()
                        .map(pythonPackageName ->
                                ImmutableSet.of(new TopLevelAddingPackageNameProcessor(pythonPackageName)))
                        .orElseGet(ImmutableSet::of))
                .addProcessors(FlatteningPackageNameProcessor.INSTANCE);
        return compoundPackageNameProcessor.build();
    }
}
