# (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
import pickle


# TODO(forozco): Once we split out code gen have more granular testing of code evaluation
def test_code_gen_compiles():
    from generated_integration.product import RecursiveObjectExample, RecursiveObjectAlias
    object_example = RecursiveObjectExample(None)
    object_alias = RecursiveObjectAlias(None)
    assert object_alias == object_example


def test_code_gen_is_picklable():
    from generated_integration.product import RecursiveObjectExample, RecursiveObjectAlias
    object_example = RecursiveObjectExample(None)
    object_alias = RecursiveObjectAlias(None)
    assert object_alias == object_example
    pickled_object = pickle.dumps(object_example, pickle.HIGHEST_PROTOCOL)
    pickled_alias = pickle.dumps(object_alias, pickle.HIGHEST_PROTOCOL)
    unpickled_object = pickle.loads(pickled_object)
    unpickled_alias = pickle.loads(pickled_alias)
    assert unpickled_object == object_example
    assert unpickled_alias == object_alias


def test_import_circular_package_reference():
    from generated_integration.product_a import Foo, Operation
    from generated_integration.product_b import Bar
    Foo(Bar(value=1, operation=Operation("operation")))


def test_union_visitor():
    from generated_integration.product import OptionsUnion, OptionsUnionVisitor

    class TestOptionsUnionVisitor(OptionsUnionVisitor):
        def _options(self, value):
            return value

    # test for backwards compatibility
    assert OptionsUnion(options="options").accept(TestOptionsUnionVisitor()) == "options"
    assert OptionsUnion(options="options", type_of_union="options").accept(TestOptionsUnionVisitor()) == "options"
