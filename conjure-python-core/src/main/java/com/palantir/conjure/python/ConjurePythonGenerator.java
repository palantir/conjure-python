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

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.python.poet.PythonAll;
import com.palantir.conjure.python.poet.PythonClass;
import com.palantir.conjure.python.poet.PythonClassName;
import com.palantir.conjure.python.poet.PythonFile;
import com.palantir.conjure.python.poet.PythonImport;
import com.palantir.conjure.python.poet.PythonLine;
import com.palantir.conjure.python.poet.PythonMetaYaml;
import com.palantir.conjure.python.poet.PythonSetup;
import com.palantir.conjure.python.util.TypeNameVisitor;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.ServiceDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.TypeName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ConjurePythonGenerator {

    private PythonFileGenerator<TypeDefinition> beanGenerator;
    private PythonFileGenerator<ServiceDefinition> serviceGenerator;
    private final GeneratorConfiguration config;

    public ConjurePythonGenerator(
            PythonFileGenerator<TypeDefinition> beanGenerator,
            PythonFileGenerator<ServiceDefinition> serviceGenerator,
            GeneratorConfiguration config) {
        this.beanGenerator = beanGenerator;
        this.serviceGenerator = serviceGenerator;
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
        Map<TypeName, TypeDefinition> knownTypes = new HashMap<>();
        TypeDefinition.Visitor<TypeName> indexingVisitor = new TypeNameVisitor();
        conjureDefinition.getTypes()
                .forEach(typeDefinition -> knownTypes.put(typeDefinition.accept(indexingVisitor), typeDefinition));

        String pythonicPackageName = config.packageName().replace('-', '_');
        PackageNameProcessor packageNameProcessor = PackageNameProcessor.builder()
                .addProcessors(new TwoComponentStrippingPackageNameProcessor())
                .addProcessors(new FlatteningPackageNameProcessor())
                .addProcessors(new TopLevelAddingPackageNameProcessor(pythonicPackageName))
                .build();

        List<PythonFile> beanClasses = knownTypes.values()
                .stream()
                .map(objectDefinition -> beanGenerator.generateFile(knownTypes, packageNameProcessor, objectDefinition))
                .collect(Collectors.toList());

        List<PythonFile> serviceClasses = conjureDefinition.getServices()
                .stream()
                .map(serviceDef -> serviceGenerator.generateFile(knownTypes, packageNameProcessor, serviceDef))
                .collect(Collectors.toList());

        Map<String, List<PythonFile>> filesByPackageName = Stream.concat(beanClasses.stream(), serviceClasses.stream())
                .collect(Collectors.groupingBy(PythonFile::packageName));

        List<PythonFile> moduleInitFiles = filesByPackageName.entrySet()
                .stream()
                .map(entry -> buildModuleInit(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        List<PythonFile> rootInit = ImmutableList.of(buildRootInit(pythonicPackageName, filesByPackageName.keySet()));

        return Stream.of(
                beanClasses.stream(),
                serviceClasses.stream(),
                moduleInitFiles.stream(),
                rootInit.stream())
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    private PythonFile buildModuleInit(String packageName, List<PythonFile> moduleFiles) {
        return PythonFile.builder()
                .packageName(packageName)
                .fileName("__init__.py")
                .imports(moduleFiles.stream()
                        .flatMap(file -> file.contents().stream()
                                .map(pythonClass -> PythonImport.of(
                                        PythonClassName.of(
                                                // File has a file extension .py
                                                "." + file.fileName().substring(0, file.fileName().length() - 3),
                                                pythonClass.className()))))
                        .collect(Collectors.toList()))
                .addContents(PythonAll.builder()
                        .addAllContents(moduleFiles.stream()
                                .flatMap(file -> file.contents().stream())
                                .map(PythonClass::className)
                                .sorted()
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }

    private PythonFile buildRootInit(String packageName, Set<String> submodules) {
        return PythonFile.builder()
                .packageName(packageName)
                .addContents(PythonAll.builder()
                        .addAllContents(submodules.stream()
                                .map(packagePath -> packagePath.substring(packageName.length() + 1))
                                .sorted()
                                .collect(Collectors.toList()))
                        .build())
                .addContents(PythonLine.builder()
                        .text(String.format("__version__ = \"%s\"", config.packageVersion()))
                        .build())
                .build();
    }

    private PythonFile buildPythonSetupFile() {
        PythonSetup.Builder builder = PythonSetup.builder()
                .putOptions("name", config.packageName())
                .putOptions("version", config.packageVersion())
                .addInstallDependencies("requests", "typing")
                .addInstallDependencies(String.format("conjure-client>=%s,<%s",
                        config.minConjureClientVersion(), config.maxConjureClientVersion()));
        config.packageDescription().ifPresent(value -> builder.putOptions("description", value));
        config.packageUrl().ifPresent(value -> builder.putOptions("url", value));
        config.packageAuthor().ifPresent(value -> builder.putOptions("author", value));
        PythonSetup setup = builder.build();

        return PythonFile.builder()
                .fileName("setup.py")
                .addContents(setup)
                .build();
    }

    private PythonFile buildCondaMetaYamlFile() {
        PythonMetaYaml metaYaml = PythonMetaYaml.builder()
                .condaPackageName(config.packageName())
                .packageVersion(config.packageVersion())
                .addInstallDependencies("requests", "typing")
                .addInstallDependencies(String.format("conjure-client >=%s,<%s",
                        config.minConjureClientVersion(), config.maxConjureClientVersion()))
                .build();

        return PythonFile.builder()
                .packageName("conda_recipe")
                .fileName("meta.yaml")
                .addContents(metaYaml)
                .build();
    }
}
