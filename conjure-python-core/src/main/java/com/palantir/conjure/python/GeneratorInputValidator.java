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

import com.palantir.conjure.defs.SafetyDeclarationRequirements;
import com.palantir.conjure.defs.validator.ConjureDefinitionValidator;
import com.palantir.conjure.defs.validator.EndpointDefinitionValidator;
import com.palantir.conjure.defs.validator.EnumDefinitionValidator;
import com.palantir.conjure.defs.validator.ErrorDefinitionValidator;
import com.palantir.conjure.defs.validator.ObjectDefinitionValidator;
import com.palantir.conjure.defs.validator.ServiceDefinitionValidator;
import com.palantir.conjure.defs.validator.TypeNameValidator;
import com.palantir.conjure.defs.validator.UnionDefinitionValidator;
import com.palantir.conjure.spec.AliasDefinition;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.spec.EnumDefinition;
import com.palantir.conjure.spec.ObjectDefinition;
import com.palantir.conjure.spec.TypeDefinition;
import com.palantir.conjure.spec.UnionDefinition;
import com.palantir.conjure.visitor.DealiasingTypeVisitor;
import com.palantir.conjure.visitor.TypeDefinitionVisitor;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Re-runs the Conjure compiler's identifier and format validation over a deserialized {@link ConjureDefinition}
 * before it is used to emit Python source.
 *
 * <p>conjure-python consumes compiled Conjure IR directly (Jackson) and never runs the compiler's parse-time
 * validation, so a hand-crafted or tampered IR can carry an identifier that violates Conjure's grammar straight into
 * a code-emitting sink — e.g. an enum value emitted verbatim as a class-body statement. Validating here rejects such
 * inputs before any source is generated.
 *
 * <p>Note that {@link ConjureDefinitionValidator#validateAll} alone is <em>not</em> sufficient: it runs only
 * cross-cutting checks (unique names, version, recursion, log-safety) and does not format-check per-type identifiers
 * such as enum values — the compiler invokes those per type while parsing. This validator therefore walks every type,
 * error and service the way the compiler does, in addition to the cross-cutting checks.
 */
public final class GeneratorInputValidator {

    private GeneratorInputValidator() {}

    public static void validate(ConjureDefinition definition) {
        DealiasingTypeVisitor dealiasingTypeVisitor = new DealiasingTypeVisitor(definition.getTypes().stream()
                .collect(Collectors.toMap(type -> type.accept(TypeDefinitionVisitor.TYPE_NAME), Function.identity())));

        definition.getTypes().forEach(type -> type.accept(TypeValidator.INSTANCE));
        definition.getErrors().forEach(ErrorDefinitionValidator::validate);
        definition.getServices().forEach(service -> {
            ServiceDefinitionValidator.validateAll(service);
            service.getEndpoints()
                    .forEach(endpoint -> EndpointDefinitionValidator.validateAll(endpoint, dealiasingTypeVisitor));
        });

        ConjureDefinitionValidator.validateAll(definition, SafetyDeclarationRequirements.ALLOWED);
    }

    private enum TypeValidator implements TypeDefinition.Visitor<Void> {
        INSTANCE;

        @Override
        public Void visitAlias(AliasDefinition value) {
            TypeNameValidator.validate(value.getTypeName());
            return null;
        }

        @Override
        public Void visitEnum(EnumDefinition value) {
            TypeNameValidator.validate(value.getTypeName());
            EnumDefinitionValidator.validateAll(value);
            return null;
        }

        @Override
        public Void visitObject(ObjectDefinition value) {
            TypeNameValidator.validate(value.getTypeName());
            ObjectDefinitionValidator.validate(value);
            return null;
        }

        @Override
        public Void visitUnion(UnionDefinition value) {
            TypeNameValidator.validate(value.getTypeName());
            UnionDefinitionValidator.validateAll(value);
            return null;
        }

        @Override
        public Void visitUnknown(String _unknownType) {
            return null;
        }
    }
}
