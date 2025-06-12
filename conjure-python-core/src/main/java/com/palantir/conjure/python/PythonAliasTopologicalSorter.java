/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.palantir.conjure.python.poet.AliasSnippet;
import com.palantir.conjure.spec.ExternalReference;
import com.palantir.conjure.spec.ListType;
import com.palantir.conjure.spec.MapType;
import com.palantir.conjure.spec.OptionalType;
import com.palantir.conjure.spec.PrimitiveType;
import com.palantir.conjure.spec.SetType;
import com.palantir.conjure.spec.Type;
import com.palantir.conjure.spec.TypeName;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PythonAliasTopologicalSorter {
    private PythonAliasTopologicalSorter() {}

    public static List<AliasSnippet> getSortedSnippets(List<AliasSnippet> snippets) {
        AliasEdgeVisitor aliasEdgeVisitor = new AliasEdgeVisitor(snippets);
        MutableGraph<AliasSnippet> mutableGraph =
                GraphBuilder.directed().nodeOrder(ElementOrder.insertion()).build();

        snippets.forEach(mutableGraph::addNode);
        snippets.forEach(snippet -> snippet.aliasType().getAlias().accept(aliasEdgeVisitor).stream()
                .filter(dependant -> !dependant.equals(snippet))
                .forEach(dependant -> mutableGraph.putEdge(dependant, snippet)));

        ImmutableList.Builder<AliasSnippet> outputBuilder = ImmutableList.builder();
        Set<AliasSnippet> roots = mutableGraph.nodes().stream()
                .filter(node -> mutableGraph.inDegree(node) == 0)
                .collect(Collectors.toSet());
        Map<AliasSnippet, Integer> nonRootsToInDegree = mutableGraph.nodes().stream()
                .filter(node -> mutableGraph.inDegree(node) > 0)
                .collect(Collectors.toMap(node -> node, mutableGraph::inDegree, (a, _b) -> a));

        // Kahn's Algorithm https://en.wikipedia.org/wiki/Topological_sorting#Kahn's_algorithm
        while (!roots.isEmpty()) {
            AliasSnippet currentNode = Iterables.getFirst(roots, null);
            roots.remove(currentNode);
            outputBuilder.add(currentNode);
            for (AliasSnippet successor : mutableGraph.successors(currentNode)) {
                int inDegree = nonRootsToInDegree.get(successor) - 1;
                nonRootsToInDegree.put(successor, inDegree);
                if (inDegree == 0) {
                    nonRootsToInDegree.remove(successor);
                    roots.add(successor);
                }
            }
        }

        Preconditions.checkState(nonRootsToInDegree.isEmpty(), "graph has at least one cycle");
        return outputBuilder.build();
    }

    static class AliasEdgeVisitor implements Type.Visitor<List<AliasSnippet>> {
        private final Map<TypeName, AliasSnippet> knownAliases;

        AliasEdgeVisitor(List<AliasSnippet> snippets) {
            knownAliases = snippets.stream()
                    .collect(Collectors.toMap(snippet -> snippet.aliasType().getTypeName(), Function.identity()));
        }

        @Override
        public List<AliasSnippet> visitPrimitive(PrimitiveType _value) {
            return Collections.emptyList();
        }

        @Override
        public List<AliasSnippet> visitOptional(OptionalType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public List<AliasSnippet> visitList(ListType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public List<AliasSnippet> visitSet(SetType value) {
            return value.getItemType().accept(this);
        }

        @Override
        public List<AliasSnippet> visitMap(MapType value) {
            return ImmutableList.<AliasSnippet>builder()
                    .addAll(value.getValueType().accept(this))
                    .addAll(value.getKeyType().accept(this))
                    .build();
        }

        @Override
        public List<AliasSnippet> visitReference(TypeName value) {
            if (knownAliases.containsKey(value)) {
                return ImmutableList.of(knownAliases.get(value));
            }
            return Collections.emptyList();
        }

        @Override
        public List<AliasSnippet> visitExternal(ExternalReference _value) {
            return Collections.emptyList();
        }

        @Override
        public List<AliasSnippet> visitUnknown(String unknownType) {
            throw new IllegalStateException("Unknown definition: " + unknownType);
        }
    }
}
