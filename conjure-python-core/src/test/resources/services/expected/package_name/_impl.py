# coding=utf-8
import builtins
from conjure_python_client import (
    BinaryType,
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
    Set,
)

class another_TestService(Service):
    """
    A Markdown description of the service. "Might end with quotes"
    """

    def get_file_systems(self, auth_header: Optional[str] = None) -> Dict[str, "product_datasets_BackingFileSystem"]:
        """
        Returns a mapping from file system id to backing file system configuration.
        """

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = None

        _path = '/catalog/fileSystems'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), Dict[str, product_datasets_BackingFileSystem])

    def create_dataset(self, request: "product_CreateDatasetRequest", test_header_arg: Optional[str] = None, auth_header: Optional[str] = None) -> "product_datasets_Dataset":

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
            'Test-Header': test_header_arg,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = ConjureEncoder().default(request)

        _path = '/catalog/datasets'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), product_datasets_Dataset)

    def get_dataset(self, dataset_rid: str, auth_header: Optional[str] = None) -> Optional["product_datasets_Dataset"]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
            'datasetRid': dataset_rid,
        }

        _json: Any = None

        _path = '/catalog/datasets/{datasetRid}'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[product_datasets_Dataset])

    def get_raw_data(self, dataset_rid: str, auth_header: Optional[str] = None) -> Any:

        _headers: Dict[str, Any] = {
            'Accept': 'application/octet-stream',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
            'datasetRid': dataset_rid,
        }

        _json: Any = None

        _path = '/catalog/datasets/{datasetRid}/raw'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            stream=True,
            json=_json)

        _raw = _response.raw
        _raw.decode_content = True
        return _raw

    def maybe_get_raw_data(self, dataset_rid: str, auth_header: Optional[str] = None) -> Optional[Any]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
            'datasetRid': dataset_rid,
        }

        _json: Any = None

        _path = '/catalog/datasets/{datasetRid}/raw-maybe'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[BinaryType])

    def upload_raw_data(self, input: Any, auth_header: Optional[str] = None) -> None:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/octet-stream',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _data: Any = input

        _path = '/catalog/datasets/upload-raw'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            data=_data)

        return

    def get_branches(self, dataset_rid: str, page_size: Optional[int] = None, auth_header: Optional[str] = None, message: Optional[str] = None) -> List[str]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
            'Special-Message': message,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
            'pageSize': page_size,
        }

        _path_params: Dict[str, Any] = {
            'datasetRid': dataset_rid,
        }

        _json: Any = None

        _path = '/catalog/datasets/{datasetRid}/branches'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), List[str])

    def get_branches_deprecated(self, dataset_rid: str, auth_header: Optional[str] = None) -> List[str]:
        """
        Gets all branches of this dataset.
        """

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
            'datasetRid': dataset_rid,
        }

        _json: Any = None

        _path = '/catalog/datasets/{datasetRid}/branchesDeprecated'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), List[str])

    def resolve_branch(self, branch: str, dataset_rid: str, auth_header: Optional[str] = None) -> Optional[str]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
            'datasetRid': dataset_rid,
            'branch': branch,
        }

        _json: Any = None

        _path = '/catalog/datasets/{datasetRid}/branches/{branch}/resolve'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[str])

    def test_param(self, dataset_rid: str, auth_header: Optional[str] = None) -> Optional[str]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
            'datasetRid': dataset_rid,
        }

        _json: Any = None

        _path = '/catalog/datasets/{datasetRid}/testParam'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[str])

    def test_query_params(self, implicit: str, something: str, list: List[int] = [], set: List[int] = [], auth_header: Optional[str] = None) -> int:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
            'different': something,
            'implicit': implicit,
            'list': list,
            'set': set,
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = None

        _path = '/catalog/test-query-params'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), int)

    def test_boolean(self, auth_header: Optional[str] = None) -> bool:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = None

        _path = '/catalog/boolean'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), bool)

    def test_double(self, auth_header: Optional[str] = None) -> float:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = None

        _path = '/catalog/double'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), float)

    def test_integer(self, auth_header: Optional[str] = None) -> int:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _header_params: Dict[str, Any] = {
            'Authorization': auth_header,
        }

        _headers.update((k, v) for k, v in _header_params.items() if v is not None)

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = None

        _path = '/catalog/integer'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), int)


another_TestService.__name__ = "TestService"
another_TestService.__qualname__ = "TestService"
another_TestService.__module__ = "package_name.another"


class product_CreateDatasetRequest(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'path': ConjureFieldDefinition('path', str)
        }

    __slots__: List[str] = ['_file_system_id', '_path']

    def __init__(self, file_system_id: str, path: str) -> None:
        self._file_system_id = file_system_id
        self._path = path

    @builtins.property
    def file_system_id(self) -> str:
        return self._file_system_id

    @builtins.property
    def path(self) -> str:
        return self._path


product_CreateDatasetRequest.__name__ = "CreateDatasetRequest"
product_CreateDatasetRequest.__qualname__ = "CreateDatasetRequest"
product_CreateDatasetRequest.__module__ = "package_name.product"


class product_datasets_BackingFileSystem(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'base_uri': ConjureFieldDefinition('baseUri', str),
            'configuration': ConjureFieldDefinition('configuration', Dict[str, str])
        }

    __slots__: List[str] = ['_file_system_id', '_base_uri', '_configuration']

    def __init__(self, base_uri: str, configuration: Dict[str, str], file_system_id: str) -> None:
        self._file_system_id = file_system_id
        self._base_uri = base_uri
        self._configuration = configuration

    @builtins.property
    def file_system_id(self) -> str:
        """
        The name by which this file system is identified.
        """
        return self._file_system_id

    @builtins.property
    def base_uri(self) -> str:
        return self._base_uri

    @builtins.property
    def configuration(self) -> Dict[str, str]:
        return self._configuration


product_datasets_BackingFileSystem.__name__ = "BackingFileSystem"
product_datasets_BackingFileSystem.__qualname__ = "BackingFileSystem"
product_datasets_BackingFileSystem.__module__ = "package_name.product_datasets"


class product_datasets_Dataset(ConjureBeanType):

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
        """
        Uniquely identifies this dataset.
        """
        return self._rid


product_datasets_Dataset.__name__ = "Dataset"
product_datasets_Dataset.__qualname__ = "Dataset"
product_datasets_Dataset.__module__ = "package_name.product_datasets"


package_name_TypeInPackageWithTheSameNameAsRootPackage = str

