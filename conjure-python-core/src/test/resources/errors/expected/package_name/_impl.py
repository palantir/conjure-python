# coding=utf-8
import builtins
from conjure_python_client import (
    ConjureBeanType,
    ConjureDecoder,
    ConjureEncoder,
    ConjureFieldDefinition,
    OptionalTypeWrapper,
    Service,
)
from requests.adapters import (
    Response,
)
from typing import (
    Any,
    Dict,
    List,
    Optional,
)
from urllib.parse import (
    quote,
)
import uuid

class product_ContextWindowExceeded(Exception):
    """The supplied input exceeded the model's maximum context window.
    """

    ERROR_CODE = "INVALID_ARGUMENT"
    ERROR_NAMESPACE = "MioMl"
    ERROR_NAME = "MioMl:ContextWindowExceeded"

    def __init__(self, input_token_count: int, max_tokens: int, error_instance_id: Optional[str] = None) -> None:
        self.input_token_count = input_token_count
        self.max_tokens = max_tokens
        self.error_instance_id = error_instance_id if error_instance_id is not None else str(uuid.uuid4())
        super().__init__(self.ERROR_NAME)

    def encode(self) -> Dict[str, Any]:
        return {
            'errorCode': self.ERROR_CODE,
            'errorName': self.ERROR_NAME,
            'errorInstanceId': self.error_instance_id,
            'parameters': {
                'inputTokenCount': ConjureEncoder.do_encode(self.input_token_count),
                'maxTokens': ConjureEncoder.do_encode(self.max_tokens)
            }
        }

    @builtins.classmethod
    def decode(cls, error: Dict[str, Any]) -> 'product_ContextWindowExceeded':
        if error.get('errorName') != cls.ERROR_NAME:
            raise ValueError(f"Error '{error.get('errorName')}' is not a {cls.ERROR_NAME}")
        decoder = ConjureDecoder()
        parameters = error.get('parameters', {})
        return cls(
            input_token_count=decoder.decode(parameters.get('inputTokenCount'), int),
            max_tokens=decoder.decode(parameters.get('maxTokens'), int),
            error_instance_id=error.get('errorInstanceId')
        )


product_ContextWindowExceeded.__name__ = "ContextWindowExceeded"
product_ContextWindowExceeded.__qualname__ = "ContextWindowExceeded"
product_ContextWindowExceeded.__module__ = "package_name.product"


class product_Dataset(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'rid': ConjureFieldDefinition('rid', str)
        }

    __slots__: List[str] = ['_file_system_id', '_rid']

    def __init__(self, file_system_id: str, rid: str) -> None:
        self._file_system_id = file_system_id
        self._rid = rid

    @builtins.property
    def file_system_id(self) -> str:
        return self._file_system_id

    @builtins.property
    def rid(self) -> str:
        return self._rid


product_Dataset.__name__ = "Dataset"
product_Dataset.__qualname__ = "Dataset"
product_Dataset.__module__ = "package_name.product"


class product_DatasetNotFound(Exception):
    """Thrown when the requested dataset does not exist
    """

    ERROR_CODE = "NOT_FOUND"
    ERROR_NAMESPACE = "Datasets"
    ERROR_NAME = "Datasets:DatasetNotFound"

    def __init__(self, dataset_rid: str, available_datasets: List[str], error_instance_id: Optional[str] = None) -> None:
        self.dataset_rid = dataset_rid
        self.available_datasets = available_datasets
        self.error_instance_id = error_instance_id if error_instance_id is not None else str(uuid.uuid4())
        super().__init__(self.ERROR_NAME)

    def encode(self) -> Dict[str, Any]:
        return {
            'errorCode': self.ERROR_CODE,
            'errorName': self.ERROR_NAME,
            'errorInstanceId': self.error_instance_id,
            'parameters': {
                'datasetRid': ConjureEncoder.do_encode(self.dataset_rid),
                'availableDatasets': ConjureEncoder.do_encode(self.available_datasets)
            }
        }

    @builtins.classmethod
    def decode(cls, error: Dict[str, Any]) -> 'product_DatasetNotFound':
        if error.get('errorName') != cls.ERROR_NAME:
            raise ValueError(f"Error '{error.get('errorName')}' is not a {cls.ERROR_NAME}")
        decoder = ConjureDecoder()
        parameters = error.get('parameters', {})
        return cls(
            dataset_rid=decoder.decode(parameters.get('datasetRid'), str),
            available_datasets=decoder.decode(parameters.get('availableDatasets'), List[str]),
            error_instance_id=error.get('errorInstanceId')
        )


product_DatasetNotFound.__name__ = "DatasetNotFound"
product_DatasetNotFound.__qualname__ = "DatasetNotFound"
product_DatasetNotFound.__module__ = "package_name.product"


class product_DatasetService(Service):

    def get_datasets_by_file_system(self, file_system_id: str) -> List["product_Dataset"]:
        """Get datasets by file system
        """
        _conjure_encoder = ConjureEncoder()

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, str] = {
            'fileSystemId': quote(str(_conjure_encoder.default(file_system_id)), safe=''),
        }

        _json: Any = None

        _path = '/datasets/fileSystem/{fileSystemId}'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), List[product_Dataset], self._return_none_for_unknown_union_types)


product_DatasetService.__name__ = "DatasetService"
product_DatasetService.__qualname__ = "DatasetService"
product_DatasetService.__module__ = "package_name.product"


class product_InvalidFileSystemId(Exception):
    """Thrown when a file system identifier is invalid
    """

    ERROR_CODE = "INVALID_ARGUMENT"
    ERROR_NAMESPACE = "Datasets"
    ERROR_NAME = "Datasets:InvalidFileSystemId"

    def __init__(self, file_system_id: str, reason: Optional[str], user_id: str, error_instance_id: Optional[str] = None) -> None:
        self.file_system_id = file_system_id
        self.reason = reason
        self.user_id = user_id
        self.error_instance_id = error_instance_id if error_instance_id is not None else str(uuid.uuid4())
        super().__init__(self.ERROR_NAME)

    def encode(self) -> Dict[str, Any]:
        return {
            'errorCode': self.ERROR_CODE,
            'errorName': self.ERROR_NAME,
            'errorInstanceId': self.error_instance_id,
            'parameters': {
                'fileSystemId': ConjureEncoder.do_encode(self.file_system_id),
                'reason': ConjureEncoder.do_encode(self.reason),
                'userId': ConjureEncoder.do_encode(self.user_id)
            }
        }

    @builtins.classmethod
    def decode(cls, error: Dict[str, Any]) -> 'product_InvalidFileSystemId':
        if error.get('errorName') != cls.ERROR_NAME:
            raise ValueError(f"Error '{error.get('errorName')}' is not a {cls.ERROR_NAME}")
        decoder = ConjureDecoder()
        parameters = error.get('parameters', {})
        return cls(
            file_system_id=decoder.decode(parameters.get('fileSystemId'), str),
            reason=decoder.decode(parameters.get('reason'), OptionalTypeWrapper[str]),
            user_id=decoder.decode(parameters.get('userId'), str),
            error_instance_id=error.get('errorInstanceId')
        )


product_InvalidFileSystemId.__name__ = "InvalidFileSystemId"
product_InvalidFileSystemId.__qualname__ = "InvalidFileSystemId"
product_InvalidFileSystemId.__module__ = "package_name.product"


