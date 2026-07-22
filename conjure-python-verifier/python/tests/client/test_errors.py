# (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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
from generated_integration.product import DatasetNotFound, InvalidFileSystemId


def test_encode_produces_serializable_error():
    err = DatasetNotFound(dataset_rid="ri.dataset.1", available_datasets=["ri.dataset.2"])
    encoded = err.encode()
    assert encoded["errorName"] == "Datasets:DatasetNotFound"
    assert encoded["errorCode"] == "NOT_FOUND"
    assert encoded["parameters"] == {"datasetRid": "ri.dataset.1", "availableDatasets": ["ri.dataset.2"]}
    assert len(encoded["errorInstanceId"]) == 36


def test_encode_decode_round_trip():
    err = DatasetNotFound(dataset_rid="ri.dataset.1", available_datasets=["ri.dataset.2"])
    decoded = DatasetNotFound.decode(err.encode())
    assert decoded.dataset_rid == "ri.dataset.1"
    assert decoded.available_datasets == ["ri.dataset.2"]
    assert decoded.error_instance_id == err.error_instance_id


def test_optional_argument_round_trips_absent_and_present():
    absent = InvalidFileSystemId(file_system_id="fs1", reason=None, user_id="u1")
    assert InvalidFileSystemId.decode(absent.encode()).reason is None
    present = InvalidFileSystemId(file_system_id="fs1", reason="bad", user_id="u1")
    assert InvalidFileSystemId.decode(present.encode()).reason == "bad"


def test_decode_rejects_mismatched_error_name():
    with pytest.raises(ValueError):
        DatasetNotFound.decode({"errorName": "Datasets:SomethingElse", "parameters": {}})
