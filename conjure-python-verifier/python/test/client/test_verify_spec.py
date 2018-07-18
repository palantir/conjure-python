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
    if endpoint_name in body_black_list and case in body_black_list[endpoint_name]:
        pytest.skip("Blacklisted")

    if should_pass:
        return confirm_service.confirm(endpoint_name, index, getattr(body_service, method_name)(index))
    else:
        with pytest.raises(Exception):
            getattr(body_service, method_name)(index)


@pytest.mark.parametrize('endpoint_name,method_name,index,value', generate_param_tests('singleHeaderService'))
def test_header(conjure_validation_server, test_black_list, header_service, endpoint_name, method_name, index, value):
    header_black_list = test_black_list['singleHeaderService']
    if endpoint_name in header_black_list and value in header_black_list[endpoint_name]:
        pytest.skip("Blacklisted")

    return getattr(header_service, method_name)(index, json.loads(value))


@pytest.mark.parametrize('endpoint_name,method_name,index,value', generate_param_tests('singlePathParamService'))
def test_path(conjure_validation_server, test_black_list, path_service, endpoint_name, method_name, index, value):
    header_black_list = test_black_list['singlePathParamService']
    if True or endpoint_name in header_black_list and value in header_black_list[endpoint_name]:
        pytest.skip("Blacklisted")
    return getattr(path_service, method_name)(index, json.loads(value))


@pytest.mark.parametrize('endpoint_name,method_name,index,value', generate_param_tests('singleQueryParamService'))
def test_query(conjure_validation_server, test_black_list, query_service, endpoint_name, method_name, index, value):
    query_black_list = test_black_list['singleQueryService']
    if endpoint_name in query_black_list and value in query_black_list[endpoint_name]:
        pytest.skip("Blacklisted")
    return getattr(query_service, method_name)(index, json.loads(value))
