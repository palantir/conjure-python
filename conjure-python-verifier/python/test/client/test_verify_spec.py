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

import inspect
import json
import pytest
import re
from os import path

CUR_DIR = path.dirname(__file__)
TEST_CASES = CUR_DIR + '/../../../build/test-cases/test-cases.json'


def convert_to_snake_case(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()


def load_test_cases():
    return json.load(open(TEST_CASES))['client']


def generate_auto_deserialize_tests():
    test_cases = load_test_cases()
    all_cases = []
    for endpoint_name, test_kinds in test_cases['autoDeserialize'].items():
        method_name = convert_to_snake_case(endpoint_name)
        positive_count = len(test_kinds['positive'])
        all_cases.extend([(endpoint_name, method_name, i, case, True)
                          for i, case in enumerate(test_kinds['positive'])])
        all_cases.extend([(endpoint_name, method_name, i + positive_count, case, False)
                          for i, case in enumerate(test_kinds['negative'])])

    return all_cases


def generate_param_tests(test_kind):
    test_cases = load_test_cases()
    return [(endpoint_name, convert_to_snake_case(endpoint_name), i, value)
            for endpoint_name, test_kinds in test_cases[test_kind].items()
            for i, value in enumerate(test_kinds)]


def run_test(should_pass, is_blacklisted, runnable):
    # If the test is blacklisted, we inverse the logic, to ensure that it would have "failed" the normal test.
    if should_pass ^ is_blacklisted:
        runnable()
    else:
        with pytest.raises(Exception):
            runnable()
    # If it did behave as intended, then skip it in the end if it was blacklisted.
    if is_blacklisted:
        pytest.skip("Blacklisted")


def run_test_with_argument(index, is_blacklisted, service, method_name, value):
    target_method = getattr(service, method_name)
    target_argspec = inspect.getargspec(target_method)
    target_param = [e
                    for e in target_argspec.args
                    if e not in ('self', 'index')][0]
    kwargs = {'index': index, target_param: json.loads(value)}

    run_test(True, is_blacklisted, lambda: target_method(**kwargs))


@pytest.mark.parametrize('endpoint_name,method_name,index,case,should_pass', generate_auto_deserialize_tests())
def test_body(
        conjure_validation_server,
        test_black_list,
        body_service,
        confirm_service,
        endpoint_name,
        method_name,
        index,
        case,
        should_pass):
    body_black_list = test_black_list['autoDeserialize']
    is_blacklisted = endpoint_name in body_black_list and case in body_black_list[endpoint_name]

    if should_pass:
        run_test(True,
                 is_blacklisted,
                 lambda: confirm_service.confirm(getattr(body_service, method_name)(index), endpoint_name, index))
    else:
        run_test(False, is_blacklisted, lambda: getattr(body_service, method_name)(index))


@pytest.mark.parametrize('endpoint_name,method_name,index,value', generate_param_tests('singleHeaderService'))
def test_header(conjure_validation_server, test_black_list, header_service, endpoint_name, method_name, index, value):
    header_black_list = test_black_list['singleHeaderService']
    is_blacklisted = endpoint_name in header_black_list and value in header_black_list[endpoint_name]
    run_test_with_argument(index, is_blacklisted, header_service, method_name, value)


@pytest.mark.parametrize('endpoint_name,method_name,index,value', generate_param_tests('singlePathParamService'))
def test_path(conjure_validation_server, test_black_list, path_service, endpoint_name, method_name, index, value):
    header_black_list = test_black_list['singlePathParamService']
    is_blacklisted = endpoint_name in header_black_list and value in header_black_list[endpoint_name]
    run_test_with_argument(index, is_blacklisted, path_service, method_name, value)


@pytest.mark.parametrize('endpoint_name,method_name,index,value', generate_param_tests('singleQueryParamService'))
def test_query(conjure_validation_server, test_black_list, query_service, endpoint_name, method_name, index, value):
    query_black_list = test_black_list['singleQueryService']
    is_blacklisted = endpoint_name in query_black_list and value in query_black_list[endpoint_name]
    run_test_with_argument(index, is_blacklisted, query_service, method_name, value)
