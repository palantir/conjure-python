# coding=utf-8
import builtins
from conjure_python_client import (
    ConjureBeanType,
    ConjureDecoder,
    ConjureEncoder,
    ConjureFieldDefinition,
    ConjureHTTPError,
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
    TypedDict,
)
from urllib.parse import (
    quote,
)

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


class product_DatasetNotFound(ConjureHTTPError):
    """Thrown when the requested dataset does not exist
    """

    ERROR_CODE = "NOT_FOUND"
    ERROR_NAMESPACE = "Datasets"
    ERROR_NAME = "Datasets:DatasetNotFound"

    class SafeArgs(TypedDict):
        dataset_rid: str
        available_datasets: List[str]

    def __init__(self, base_error: ConjureHTTPError) -> None:
        super().__init__(
            status_code=base_error.status_code,
            error_code=base_error.error_code,
            error_name=base_error.error_name,
            error_instance_id=base_error.error_instance_id,
            parameters=base_error.parameters
        )
        self.safe_args: product_DatasetNotFound.SafeArgs = {
            'dataset_rid': base_error.parameters['datasetRid'],
            'available_datasets': base_error.parameters['availableDatasets']
        }

    @builtins.classmethod
    def is_instance(cls, error: ConjureHTTPError) -> bool:
        return error.error_name == cls.ERROR_NAME

    @builtins.classmethod
    def from_error(cls, error: ConjureHTTPError) -> 'product_DatasetNotFound':
        if not cls.is_instance(error):
            raise ValueError(f"Error '{error.error_name}' is not a {cls.ERROR_NAME}")
        return cls(error)


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


class product_InvalidFileSystemId(ConjureHTTPError):
    """Thrown when a file system identifier is invalid
    """

    ERROR_CODE = "INVALID_ARGUMENT"
    ERROR_NAMESPACE = "Datasets"
    ERROR_NAME = "Datasets:InvalidFileSystemId"

    class SafeArgs(TypedDict):
        file_system_id: str
        reason: Optional[str]

    class UnsafeArgs(TypedDict):
        user_id: str

    def __init__(self, base_error: ConjureHTTPError) -> None:
        super().__init__(
            status_code=base_error.status_code,
            error_code=base_error.error_code,
            error_name=base_error.error_name,
            error_instance_id=base_error.error_instance_id,
            parameters=base_error.parameters
        )
        self.safe_args: product_InvalidFileSystemId.SafeArgs = {
            'file_system_id': base_error.parameters['fileSystemId'],
            'reason': base_error.parameters.get('reason')
        }
        self.unsafe_args: product_InvalidFileSystemId.UnsafeArgs = {
            'user_id': base_error.parameters['userId']
        }

    @builtins.classmethod
    def is_instance(cls, error: ConjureHTTPError) -> bool:
        return error.error_name == cls.ERROR_NAME

    @builtins.classmethod
    def from_error(cls, error: ConjureHTTPError) -> 'product_InvalidFileSystemId':
        if not cls.is_instance(error):
            raise ValueError(f"Error '{error.error_name}' is not a {cls.ERROR_NAME}")
        return cls(error)


product_InvalidFileSystemId.__name__ = "InvalidFileSystemId"
product_InvalidFileSystemId.__qualname__ = "InvalidFileSystemId"
product_InvalidFileSystemId.__module__ = "package_name.product"


