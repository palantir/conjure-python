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

import pytest
import subprocess
import yaml
from conjure_python_client import RequestsClient, ServiceConfiguration
from os import path

from ..generated.conjure_verification_server import (
    AutoDeserializeConfirmService,
    AutoDeserializeService,
    SingleHeaderService,
    SinglePathParamService,
    SingleQueryParamService
)

TEST_CASES = path.dirname(__file__) + '/../../../build/test-cases/test-cases.json'
VERIFICATION_API = path.dirname(__file__) + '/../../../build/test-cases/verification-api.json'


@pytest.fixture(scope='module')
def conjure_validation_server():
    verification_server = subprocess.Popen([
        path.dirname(__file__) + '/../../../build/verification-server/server', TEST_CASES, VERIFICATION_API])
    yield verification_server
    verification_server.terminate()


@pytest.fixture()
def config():
    config = ServiceConfiguration()
    config.uris = ['http://localhost:8000']
    return config


@pytest.fixture()
def body_service(config):
    return RequestsClient.create(AutoDeserializeService, 'conjure-python/0.0.0', config)


@pytest.fixture()
def header_service(config):
    return RequestsClient.create(SingleHeaderService, 'conjure-python/0.0.0', config)


@pytest.fixture()
def path_service(config):
    return RequestsClient.create(SinglePathParamService, 'conjure-python/0.0.0', config)


@pytest.fixture()
def query_service(config):
    return RequestsClient.create(SingleQueryParamService, 'conjure-python/0.0.0', config)


@pytest.fixture()
def confirm_service(config):
    return RequestsClient.create(AutoDeserializeConfirmService, 'conjure-python/0.0.0', config)

@pytest.fixture(scope='module')
def test_black_list():
    with open(path.dirname(__file__) + '/../../../resources/ignored_test_cases.yml') as blacklist_file:
        return yaml.load(blacklist_file)['client']
