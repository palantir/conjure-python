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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.palantir.conjure.python.client.ClientGenerator;
import com.palantir.conjure.python.poet.PythonAll;
import com.palantir.conjure.python.poet.PythonClass;
import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonLine;
import com.palantir.conjure.python.poet.PythonMetaYaml;
import com.palantir.conjure.python.poet.PythonSetup;
import com.palantir.conjure.python.types.PythonBeanGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ConjurePythonGenerator {

    private final PythonBeanGenerator beanGenerator;
    private final ClientGenerator clientGenerator;
    private final GeneratorConfiguration config;

    public ConjurePythonGenerator(PythonBeanGenerator beanGenerator, ClientGenerator clientGenerator,
            GeneratorConfiguration config) {
        this.beanGenerator = beanGenerator;
        this.clientGenerator = clientGenerator;
        this.config = config;
    }

    public void write(ConjureDefinition conjureDefinition, PythonFileWriter writer) {
        generate(conjureDefinition).forEach(writer::writePythonFile);
        writer.writePythonFile(buildPythonSetupFile());
        if (config.shouldWriteCondaRecipe()) {
            writer.writePythonFile(buildCondaMetaYamlFile());
        }
    }

    public List<PythonFile> generate(ConjureDefinition conjureDefinition) {
        List<TypeDefinition> types = conjureDefinition.getTypes();

        String pythonicPackageName = config.packageName().replace('-', '_');
        PackageNameProcessor packageNameProcessor = PackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .addProcessors(new FlatteningPackageNameProcessor())
                .addProcessors(new TopLevelAddingPackageNameProcessor(pythonicPackageName))
                .build();

        List<PythonClass> beanClasses = types
                .stream()
                .map(objectDefinition -> beanGenerator.generateObject(types, packageNameProcessor, objectDefinition))
                .collect(Collectors.toList());

        List<PythonClass> serviceClasses = conjureDefinition.getServices()
                .stream()
                .map(serviceDef -> clientGenerator.generateClient(types, packageNameProcessor, serviceDef))
                .collect(Collectors.toList());

        Map<String, List<PythonClass>> classesByPackageName =
                Stream.concat(beanClasses.stream(), serviceClasses.stream())
                        .collect(Collectors.groupingBy(PythonClass::packageName));

        // group into files
        List<PythonFile.Builder> pythonFiles = classesByPackageName.entrySet()
                .stream()
                .map(entry -> PythonFile.builder()
                        .packageName(entry.getKey())
                        .addAllImports(entry.getValue()
                                .stream()
                                .flatMap(pt -> pt.requiredImports().stream())
                                .collect(Collectors.toSet()))
                        .addAllContents(entry.getValue()))
                .collect(Collectors.toList());

        Map<Path, Set<Path>> initDefinitions = Maps.newHashMap();
        pythonFiles.stream()
                .flatMap(f -> getIntermediateInitPaths(f.build()).entrySet().stream())
                .forEach(e -> initDefinitions.merge(e.getKey(), e.getValue(), (v1, v2) -> {
                    Set<Path> combined = Sets.newHashSet(v1);
                    combined.addAll(v2);
                    return combined;
                }));
        List<PythonFile.Builder> initFiles = initDefinitions.entrySet().stream()
                .map(e -> buildInitPythonFile(e.getKey(), e.getValue()))
                .filter(f -> !classesByPackageName.keySet().contains(f.build().packageName()))
                .collect(Collectors.toList());

        return Stream.concat(pythonFiles.stream(), initFiles.stream())
                .map(f -> {
                    if (f.build().packageName().indexOf('.') < 0) {
                        f.addContents(versionAttribute(f.build().packageName()));
                        f.addContents(generatorVersionAttribute(f.build().packageName()));
                    }
                    return f;
                })
                .map(PythonFile.Builder::build)
                .collect(Collectors.toList());
    }

    private static Map<Path, Set<Path>> getIntermediateInitPaths(PythonFile pythonFile) {
        Path filePath = PythonFileWriter.getPath(pythonFile).getParent();
        Map<Path, Set<Path>> initFiles = Maps.newHashMap();
        while (filePath.getParent() != null) {
            initFiles.put(filePath.getParent(), Sets.newHashSet(filePath.getFileName()));
            filePath = filePath.getParent();
        }
        return initFiles;
    }

    private PythonFile.Builder buildInitPythonFile(Path module, Set<Path> submodules) {
        String packageName = module.toString().replace('/', '.');
        PythonAll all = PythonAll.builder()
                .packageName(packageName)
                .addAllContents(submodules.stream().map(m -> m.toString()).sorted().collect(Collectors.toList()))
                .build();
        return PythonFile.builder()
                .packageName(packageName)
                .addContents(all);
    }

    private PythonClass versionAttribute(String packageName) {
        return PythonLine.builder()
                .text(String.format("__version__ = \"%s\"", config.packageVersion()))
                .packageName(packageName)
                .build();
    }

    private PythonClass generatorVersionAttribute(String packageName) {
        return PythonLine.builder()
                .text(String.format("__conjure_generator_version__ = \"%s\"", config.generatorVersion()))
                .packageName(packageName)
                .build();
    }

    private PythonFile buildPythonSetupFile() {
        PythonSetup.Builder builder = PythonSetup.builder()
                .putOptions("name", config.packageName())
                .putOptions("version", config.packageVersion())
                .addInstallDependencies("requests", "typing")
                .addInstallDependencies(String.format("conjure-python-client>=%s,<%s",
                        config.minConjureClientVersion(), config.maxConjureClientVersion()));
        config.packageDescription().ifPresent(value -> builder.putOptions("description", value));
        config.packageUrl().ifPresent(value -> builder.putOptions("url", value));
        config.packageAuthor().ifPresent(value -> builder.putOptions("author", value));
        PythonSetup setup = builder.build();

        return PythonFile.builder()
                .fileName("setup.py")
                .addAllImports(setup.requiredImports())
                .addContents(setup)
                .build();
    }

    private PythonFile buildCondaMetaYamlFile() {
        PythonMetaYaml metaYaml = PythonMetaYaml.builder()
                .condaPackageName(config.packageName())
                .packageVersion(config.packageVersion())
                .addInstallDependencies("requests", "typing")
                .addInstallDependencies(String.format("conjure-python-client >=%s,<%s",
                        config.minConjureClientVersion(), config.maxConjureClientVersion()))
                .build();

        return PythonFile.builder()
                .packageName("conda_recipe")
                .fileName("meta.yaml")
                .addContents(metaYaml)
                .build();
    }
}
