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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.palantir.conjure.python.client.ClientGenerator;
import com.palantir.conjure.python.poet.AllSnippet;
import com.palantir.conjure.python.poet.ImmutablePythonFile;
import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonLine;
import com.palantir.conjure.python.poet.PythonMetaYaml;
import com.palantir.conjure.python.poet.PythonSetup;
import com.palantir.conjure.python.poet.PythonSnippet;
import com.palantir.conjure.python.types.ImportTypeVisitor;
import com.palantir.conjure.python.types.PythonBeanGenerator;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ConjurePythonGenerator {

    private final PythonBeanGenerator beanGenerator;
    private final ClientGenerator clientGenerator;
    private final GeneratorConfiguration config;

    public ConjurePythonGenerator(
            PythonBeanGenerator beanGenerator,
            ClientGenerator clientGenerator,
            GeneratorConfiguration config) {
        Preconditions.checkArgument(
                config.generateRawSource() || (config.packageName().isPresent() && config.packageVersion().isPresent()),
                "If generateRawSource is not set, packageName and packageVersion must be present");
        Preconditions.checkArgument(!(config.generateRawSource() && config.shouldWriteCondaRecipe()),
                "If generateRawSource is set, shouldWriteCondaRecipe must not be set");
        this.beanGenerator = beanGenerator;
        this.clientGenerator = clientGenerator;
        this.config = config;
    }

    public void write(ConjureDefinition conjureDefinition, PythonFileWriter writer) {
        generate(conjureDefinition).forEach(writer::writePythonFile);
        if (!config.generateRawSource()) {
            writer.writePythonFile(buildPythonSetupFile());
        }
        if (config.shouldWriteCondaRecipe()) {
            writer.writePythonFile(buildCondaMetaYamlFile());
        }
    }

    public List<PythonFile> generate(ConjureDefinition conjureDefinition) {
        PackageNameProcessor.Builder packageNameProcessorBuilder = PackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .addProcessors(new FlatteningPackageNameProcessor());
        if (config.packageName().isPresent()) {
            String pythonicPackageName = config.packageName().get().replace('-', '_');
            packageNameProcessorBuilder.addProcessors(new TopLevelAddingPackageNameProcessor(pythonicPackageName));
        }
        PackageNameProcessor packageNameProcessor = packageNameProcessorBuilder.build();

        DealiasingTypeVisitor dealiasingTypeVisitor = new DealiasingTypeVisitor(
                conjureDefinition.getTypes()
                        .stream()
                        .collect(Collectors.toMap(type -> type.accept(TypeDefinitionVisitor.TYPE_NAME),
                                Function.identity())));

        Multimap<String, PythonSnippet> snippets = HashMultimap.create();
        conjureDefinition.getTypes().forEach(typeDefinition ->
                snippets.put(resolveTypePackage(typeDefinition),
                        beanGenerator.generateType(
                                typeDefinition,
                                typeName -> new ImportTypeVisitor(typeName, packageNameProcessor),
                                dealiasingTypeVisitor)));
        conjureDefinition.getServices().forEach(serviceDefinition ->
                snippets.put(serviceDefinition.getServiceName().getPackage(),
                        clientGenerator.generateClient(
                                serviceDefinition,
                                typeName -> new ImportTypeVisitor(typeName, packageNameProcessor),
                                dealiasingTypeVisitor)));

        ImmutableList.Builder<PythonFile> allFiles = ImmutableList.builder();
        allFiles.addAll(snippets.asMap().entrySet()
                .stream()
                .map(entry -> PythonFile.builder()
                        .packageName(packageNameProcessor.getPackageName(entry.getKey()))
                        .fileName("__init__.py")
                        .contents(new HashSet<>(entry.getValue()))
                        .build())
                .collect(Collectors.toList()));

        allFiles.add(getRootInit(packageNameProcessor, snippets));

        return allFiles.build();
    }

    private ImmutablePythonFile getRootInit(PackageNameProcessor packageNameProcessor,
            Multimap<String, PythonSnippet> snippets) {
        String rootInitFilePath = config.packageName().orElse("");
        PythonFile.Builder builder = PythonFile.builder()
                .packageName(config.packageName().orElse("."))
                .fileName("__init__.py")
                .addContents(AllSnippet.builder()
                        .contents(snippets.keySet().stream()
                                .map(name -> packageNameProcessor.getPackageName(name)
                                        .replace(rootInitFilePath, "")
                                        .replace(".", ""))
                                .sorted()
                                .collect(Collectors.toList()))
                        .build())
                .addContents(PythonLine.builder()
                        .text(String.format("__conjure_generator_version__ = \"%s\"", config.generatorVersion()))
                        .build());
        config.packageVersion().ifPresent(version -> builder.addContents(
                PythonLine.builder()
                        .text(String.format("__version__ = \"%s\"", config.packageVersion().get()))
                        .build()));
        return builder.build();
    }

    private String resolveTypePackage(TypeDefinition typeDef) {
        return typeDef.accept(new TypeDefinition.Visitor<String>() {
            @Override
            public String visitAlias(AliasDefinition value) {
                return value.getTypeName().getPackage();
            }

            @Override
            public String visitEnum(EnumDefinition value) {
                return value.getTypeName().getPackage();
            }

            @Override
            public String visitObject(ObjectDefinition value) {
                return value.getTypeName().getPackage();
            }

            @Override
            public String visitUnion(UnionDefinition value) {
                return value.getTypeName().getPackage();
            }

            @Override
            public String visitUnknown(String unknownType) {
                throw new IllegalStateException("Unsupported type: " + unknownType);
            }
        });
    }

    private PythonFile buildPythonSetupFile() {
        PythonSetup.Builder builder = PythonSetup.builder()
                .putOptions("name", config.packageName().get())
                .putOptions("version", config.packageVersion().get())
                .addInstallDependencies("requests", "typing")
                .addInstallDependencies(String.format("conjure-python-client>=%s,<%s",
                        config.minConjureClientVersion(), config.maxConjureClientVersion()));
        config.packageDescription().ifPresent(value -> builder.putOptions("description", value));
        config.packageUrl().ifPresent(value -> builder.putOptions("url", value));
        config.packageAuthor().ifPresent(value -> builder.putOptions("author", value));

        return PythonFile.builder()
                .packageName(".")
                .fileName("setup.py")
                .addContents(builder.build())
                .build();
    }

    private PythonFile buildCondaMetaYamlFile() {
        return PythonFile.builder()
                .packageName("conda_recipe")
                .fileName("meta.yaml")
                .addContents(PythonMetaYaml.builder()
                        .condaPackageName(config.packageName().get())
                        .packageVersion(config.packageVersion().get())
                        .addInstallDependencies("requests", "typing")
                        .addInstallDependencies(String.format("conjure-python-client >=%s,<%s",
                                config.minConjureClientVersion(), config.maxConjureClientVersion()))
                        .build())
                .build();
    }
}
