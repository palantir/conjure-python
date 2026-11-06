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

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.palantir.conjure.python.poet.EnumSnippet.PythonEnumValue;
import com.palantir.conjure.python.poet.PythonEndpointDefinition.PythonEndpointParam;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.HeaderParameterType;
import com.palantir.conjure.spec.HttpMethod;
import com.palantir.conjure.spec.HttpPath;
import com.palantir.conjure.spec.ParameterId;
import com.palantir.conjure.spec.ParameterType;
import com.palantir.conjure.spec.PathParameterType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.QueryParameterType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import java.util.Optional;
import org.junit.Test;

public final class PoetIdentifierValidationTest {

    private static final PythonPackage PACKAGE = PythonPackage.of("test_package");

    @Test
    public void pythonFieldRejectsInvalidAttributeName() {
        assertThatThrownBy(() -> PythonField.builder()
                        .attributeName("has space")
                        .jsonIdentifier("hasSpace")
                        .pythonType("str")
                        .myPyType("str")
                        .isOptional(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void enumSnippetRejectsInvalidEnumValue() {
        assertThatThrownBy(() -> EnumSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Color")
                        .definitionName("Color")
                        .definitionPackage(PACKAGE)
                        .values(java.util.List.of(PythonEnumValue.of("RED = 1", Optional.empty())))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void enumSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> EnumSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Color")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void beanSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> BeanSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Bean")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void unionSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> UnionSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Union")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonServiceRejectsInvalidClassName() {
        assertThatThrownBy(() -> PythonService.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .definitionName("Service")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void aliasSnippetRejectsInvalidClassName() {
        assertThatThrownBy(() -> AliasSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bad Name")
                        .aliasName("str")
                        .aliasType(AliasDefinition.builder()
                                .typeName(TypeName.of("Alias", "com.example.product"))
                                .alias(Type.primitive(PrimitiveType.STRING))
                                .build())
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonEndpointRejectsInvalidMethodName() {
        assertThatThrownBy(() -> PythonEndpointDefinition.builder()
                        .pythonMethodName("bad name")
                        .httpMethod(HttpMethod.GET)
                        .httpPath(HttpPath.of("/foo"))
                        .isRequestBinary(false)
                        .isResponseBinary(false)
                        .isOptionalReturnType(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonEndpointRejectsHttpPathThatEscapesStringLiteral() {
        // fixedPath() is emitted as _path = '<path>'; a quote would break out of the literal.
        assertThatThrownBy(() -> PythonEndpointDefinition.builder()
                        .pythonMethodName("doThing")
                        .httpMethod(HttpMethod.GET)
                        .httpPath(HttpPath.of("/foo'/bar"))
                        .isRequestBinary(false)
                        .isResponseBinary(false)
                        .isOptionalReturnType(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonEndpointRejectsHttpMethodThatEscapesStringLiteral() {
        // valueOf preserves an unknown method verbatim, and it is emitted as '<METHOD>',.
        assertThatThrownBy(() -> PythonEndpointDefinition.builder()
                        .pythonMethodName("doThing")
                        .httpMethod(HttpMethod.valueOf("GET'"))
                        .httpPath(HttpPath.of("/foo"))
                        .isRequestBinary(false)
                        .isResponseBinary(false)
                        .isOptionalReturnType(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonEndpointParamRejectsPathParamNameThatEscapesStringLiteral() {
        // paramName is emitted as the _path_params key ('<name>': quote(...)).
        assertThatThrownBy(() -> PythonEndpointParam.builder()
                        .paramName("id'")
                        .pythonParamName("id")
                        .myPyType("str")
                        .isOptional(false)
                        .isCollection(false)
                        .paramType(ParameterType.path(PathParameterType.of()))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonEndpointParamRejectsHeaderParamIdThatEscapesStringLiteral() {
        // The header ParameterId is emitted as the _headers key ('<id>': ...).
        assertThatThrownBy(() -> PythonEndpointParam.builder()
                        .paramName("authHeader")
                        .pythonParamName("auth_header")
                        .myPyType("str")
                        .isOptional(false)
                        .isCollection(false)
                        .paramType(ParameterType.header(HeaderParameterType.of(ParameterId.of("X'Y"))))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonEndpointParamRejectsQueryParamIdThatEscapesStringLiteral() {
        // The query ParameterId is emitted as the _params key ('<id>': ...).
        assertThatThrownBy(() -> PythonEndpointParam.builder()
                        .paramName("query")
                        .pythonParamName("query")
                        .myPyType("str")
                        .isOptional(false)
                        .isCollection(false)
                        .paramType(ParameterType.query(QueryParameterType.of(ParameterId.of("q'"))))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonFieldRejectsJsonIdentifierThatEscapesStringLiteral() {
        // jsonIdentifier is emitted inside ConjureFieldDefinition('<id>', ...) by Bean/Union.
        assertThatThrownBy(() -> PythonField.builder()
                        .attributeName("myField")
                        .jsonIdentifier("has'quote")
                        .pythonType("str")
                        .myPyType("str")
                        .isOptional(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void beanSnippetRejectsDefinitionNameThatEscapesStringLiteral() {
        assertThatThrownBy(() -> BeanSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bean")
                        .definitionName("Bean'")
                        .definitionPackage(PACKAGE)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void beanSnippetRejectsDefinitionPackageThatEscapesStringLiteral() {
        assertThatThrownBy(() -> BeanSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Bean")
                        .definitionName("Bean")
                        .definitionPackage(PythonPackage.of("com.evil'"))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void unionSnippetRejectsDefinitionPackageThatEscapesStringLiteral() {
        assertThatThrownBy(() -> UnionSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Union")
                        .definitionName("Union")
                        .definitionPackage(PythonPackage.of("com.evil'"))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void enumSnippetRejectsDefinitionPackageThatEscapesStringLiteral() {
        assertThatThrownBy(() -> EnumSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Color")
                        .definitionName("Color")
                        .definitionPackage(PythonPackage.of("com.evil'"))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonServiceRejectsDefinitionPackageThatEscapesStringLiteral() {
        assertThatThrownBy(() -> PythonService.builder()
                        .pythonPackage(PACKAGE)
                        .className("Service")
                        .definitionName("Service")
                        .definitionPackage(PythonPackage.of("com.evil'"))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void aliasSnippetRejectsAliasNameThatCouldRunCode() {
        // aliasName is emitted as bare code -- "<className> = <aliasName>" -- which runs when the module is imported.
        assertThatThrownBy(() -> AliasSnippet.builder()
                        .pythonPackage(PACKAGE)
                        .className("Alias")
                        .aliasName("Bad(Name)")
                        .aliasType(AliasDefinition.builder()
                                .typeName(TypeName.of("Alias", "com.example.product"))
                                .alias(Type.primitive(PrimitiveType.STRING))
                                .build())
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonFieldRejectsPythonTypeThatCouldRunCode() {
        // pythonType is emitted as an argument to ConjureFieldDefinition, so a call would be evaluated on import.
        assertThatThrownBy(() -> PythonField.builder()
                        .attributeName("myField")
                        .jsonIdentifier("myField")
                        .pythonType("Bad(Name)")
                        .myPyType("str")
                        .isOptional(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonFieldRejectsMyPyTypeThatCouldRunCode() {
        assertThatThrownBy(() -> PythonField.builder()
                        .attributeName("myField")
                        .jsonIdentifier("myField")
                        .pythonType("str")
                        .myPyType("Bad(Name)")
                        .isOptional(false)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonFieldAcceptsGenericAndForwardReferencedTypes() {
        // Generics and mypy forward references are legitimate and must not be rejected.
        assertThatCode(() -> PythonField.builder()
                        .attributeName("myField")
                        .jsonIdentifier("myField")
                        .pythonType("Dict[str, List[int]]")
                        .myPyType("Optional[\"MyObject\"]")
                        .isOptional(true)
                        .build())
                .doesNotThrowAnyException();
    }

    @Test
    public void pythonEndpointParamRejectsMyPyTypeThatCouldRunCode() {
        // myPyType is emitted as bare code in the generated method signature.
        assertThatThrownBy(() -> PythonEndpointParam.builder()
                        .paramName("id")
                        .pythonParamName("id")
                        .myPyType("Bad(Name)")
                        .isOptional(false)
                        .isCollection(false)
                        .paramType(ParameterType.path(PathParameterType.of()))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonImportRejectsModuleSpecifierThatCouldEndTheStatement() {
        // The specifier is emitted as bare code: "from <spec> import (".
        assertThatThrownBy(() -> PythonImport.builder()
                        .moduleSpecifier("evil; x")
                        .addNamedImports(NamedImport.of("Foo"))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void namedImportRejectsNameThatIsNotAnIdentifier() {
        assertThatThrownBy(() -> NamedImport.of("Bad(Name)")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void namedImportRejectsAliasThatIsNotAnIdentifier() {
        // For a cross-package reference both halves of "<name> as <alias>" come from IR type names.
        assertThatThrownBy(() -> NamedImport.of("Foo", "Bad(Name)")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void pythonImportAcceptsRelativeSpecifierAndAliasedImport() {
        assertThatCode(() -> PythonImport.builder()
                        .moduleSpecifier(".._impl")
                        .addNamedImports(NamedImport.of("Widget", "product_Widget"))
                        .build())
                .doesNotThrowAnyException();
    }

    @Test
    public void pythonEndpointAcceptsTemplatedPathAndPathParam() {
        // A legitimate templated path and a clean path param must not be rejected.
        assertThatCode(() -> PythonEndpointDefinition.builder()
                        .pythonMethodName("getRegistryItem")
                        .httpMethod(HttpMethod.GET)
                        .httpPath(HttpPath.of("/registry/{id}"))
                        .addParams(PythonEndpointParam.builder()
                                .paramName("id")
                                .pythonParamName("id")
                                .myPyType("str")
                                .isOptional(false)
                                .isCollection(false)
                                .paramType(ParameterType.path(PathParameterType.of()))
                                .build())
                        .isRequestBinary(false)
                        .isResponseBinary(false)
                        .isOptionalReturnType(false)
                        .build())
                .doesNotThrowAnyException();
    }
}
