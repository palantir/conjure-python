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

    def get_file_systems(self, auth_header: str) -> Dict[str, "product_datasets_BackingFileSystem"]:
        """
        Returns a mapping from file system id to backing file system configuration.
        """

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return _decoder.decode(_response.json(), Dict[str, product_datasets_BackingFileSystem], self._return_none_for_unknown_union_types)

    def create_dataset(self, auth_header: str, request: "product_CreateDatasetRequest", test_header_arg: str) -> "product_datasets_Dataset":

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': auth_header,
            'Test-Header': test_header_arg,
        }

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
        return _decoder.decode(_response.json(), product_datasets_Dataset, self._return_none_for_unknown_union_types)

    def get_dataset(self, auth_header: str, dataset_rid: str) -> Optional["product_datasets_Dataset"]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[product_datasets_Dataset], self._return_none_for_unknown_union_types)

    def get_raw_data(self, auth_header: str, dataset_rid: str) -> Any:

        _headers: Dict[str, Any] = {
            'Accept': 'application/octet-stream',
            'Authorization': auth_header,
        }

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

    def maybe_get_raw_data(self, auth_header: str, dataset_rid: str) -> Optional[Any]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[BinaryType], self._return_none_for_unknown_union_types)

    def upload_raw_data(self, auth_header: str, input: Any) -> None:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/octet-stream',
            'Authorization': auth_header,
        }

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

    def get_branches(self, auth_header: str, dataset_rid: str, message: Optional[str] = None, page_size: Optional[int] = None) -> List[str]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
            'Special-Message': message,
        }

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
        return _decoder.decode(_response.json(), List[str], self._return_none_for_unknown_union_types)

    def get_branches_deprecated(self, auth_header: str, dataset_rid: str) -> List[str]:
        """
        Gets all branches of this dataset.
        """

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return _decoder.decode(_response.json(), List[str], self._return_none_for_unknown_union_types)

    def resolve_branch(self, auth_header: str, branch: str, dataset_rid: str) -> Optional[str]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[str], self._return_none_for_unknown_union_types)

    def test_param(self, auth_header: str, dataset_rid: str) -> Optional[str]:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalTypeWrapper[str], self._return_none_for_unknown_union_types)

    def test_query_params(self, auth_header: str, implicit: str, something: str, list: List[int] = None, set: List[int] = None) -> int:
        list = list if list is not None else []
        set = set if set is not None else []

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return _decoder.decode(_response.json(), int, self._return_none_for_unknown_union_types)

    def test_boolean(self, auth_header: str) -> bool:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return _decoder.decode(_response.json(), bool, self._return_none_for_unknown_union_types)

    def test_double(self, auth_header: str) -> float:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return _decoder.decode(_response.json(), float, self._return_none_for_unknown_union_types)

    def test_integer(self, auth_header: str) -> int:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        }

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
        return _decoder.decode(_response.json(), int, self._return_none_for_unknown_union_types)


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

