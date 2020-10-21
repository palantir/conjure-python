import builtins
from conjure_python_client import (
    BinaryType,
    ConjureBeanType,
    ConjureDecoder,
    ConjureEncoder,
    ConjureFieldDefinition,
    DictType,
    ListType,
    OptionalType,
    Service,
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

    def get_file_systems(self, auth_header):
        # type: (str) -> Dict[str, product_datasets_BackingFileSystem]
        """
        Returns a mapping from file system id to backing file system configuration.
        """

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/fileSystems'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), DictType(str, product_datasets_BackingFileSystem))

    def create_dataset(self, auth_header, request, test_header_arg):
        # type: (str, product_CreateDatasetRequest, str) -> product_datasets_Dataset

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': auth_header,
            'Test-Header': test_header_arg,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(request) # type: Any

        _path = '/catalog/datasets'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), product_datasets_Dataset)

    def get_dataset(self, auth_header, dataset_rid):
        # type: (str, str) -> Optional[product_datasets_Dataset]

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
            'datasetRid': dataset_rid,
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/datasets/{datasetRid}'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalType(product_datasets_Dataset))

    def get_raw_data(self, auth_header, dataset_rid):
        # type: (str, str) -> Any

        _headers = {
            'Accept': 'application/octet-stream',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
            'datasetRid': dataset_rid,
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/datasets/{datasetRid}/raw'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            stream=True,
            json=_json)

        _raw = _response.raw
        _raw.decode_content = True
        return _raw

    def maybe_get_raw_data(self, auth_header, dataset_rid):
        # type: (str, str) -> Optional[Any]

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
            'datasetRid': dataset_rid,
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/datasets/{datasetRid}/raw-maybe'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalType(BinaryType()))

    def upload_raw_data(self, auth_header, input):
        # type: (str, Any) -> None

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(input) # type: Any

        _path = '/catalog/datasets/upload-raw'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        return

    def get_branches(self, auth_header, dataset_rid, message=None, page_size=None):
        # type: (str, str, Optional[str], Optional[int]) -> List[str]

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
            'Special-Message': message,
        } # type: Dict[str, Any]

        _params = {
            'pageSize': page_size,
        } # type: Dict[str, Any]

        _path_params = {
            'datasetRid': dataset_rid,
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/datasets/{datasetRid}/branches'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), ListType(str))

    def get_branches_deprecated(self, auth_header, dataset_rid):
        # type: (str, str) -> List[str]
        """
        Gets all branches of this dataset.
        """

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
            'datasetRid': dataset_rid,
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/datasets/{datasetRid}/branchesDeprecated'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), ListType(str))

    def resolve_branch(self, auth_header, branch, dataset_rid):
        # type: (str, str, str) -> Optional[str]

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
            'datasetRid': dataset_rid,
            'branch': branch,
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/datasets/{datasetRid}/branches/{branch}/resolve'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalType(str))

    def test_param(self, auth_header, dataset_rid):
        # type: (str, str) -> Optional[str]

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
            'datasetRid': dataset_rid,
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/datasets/{datasetRid}/testParam'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return None if _response.status_code == 204 else _decoder.decode(_response.json(), OptionalType(str))

    def test_query_params(self, auth_header, implicit, something):
        # type: (str, str, str) -> int

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
            'different': something,
            'implicit': implicit,
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/test-query-params'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), int)

    def test_boolean(self, auth_header):
        # type: (str) -> bool

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/boolean'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), bool)

    def test_double(self, auth_header):
        # type: (str) -> float

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/double'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), float)

    def test_integer(self, auth_header):
        # type: (str) -> int

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/integer'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
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
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'path': ConjureFieldDefinition('path', str)
        }

    __slots__ = ['_file_system_id', '_path'] # type: List[str]

    def __init__(self, file_system_id, path):
        # type: (str, str) -> None
        self._file_system_id = file_system_id
        self._path = path

    @builtins.property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @builtins.property
    def path(self):
        # type: () -> str
        return self._path


product_CreateDatasetRequest.__name__ = "CreateDatasetRequest"
product_CreateDatasetRequest.__qualname__ = "CreateDatasetRequest"
product_CreateDatasetRequest.__module__ = "package_name.product"


class product_datasets_BackingFileSystem(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'base_uri': ConjureFieldDefinition('baseUri', str),
            'configuration': ConjureFieldDefinition('configuration', DictType(str, str))
        }

    __slots__ = ['_file_system_id', '_base_uri', '_configuration'] # type: List[str]

    def __init__(self, base_uri, configuration, file_system_id):
        # type: (str, Dict[str, str], str) -> None
        self._file_system_id = file_system_id
        self._base_uri = base_uri
        self._configuration = configuration

    @builtins.property
    def file_system_id(self):
        # type: () -> str
        """
        The name by which this file system is identified.
        """
        return self._file_system_id

    @builtins.property
    def base_uri(self):
        # type: () -> str
        return self._base_uri

    @builtins.property
    def configuration(self):
        # type: () -> Dict[str, str]
        return self._configuration


product_datasets_BackingFileSystem.__name__ = "BackingFileSystem"
product_datasets_BackingFileSystem.__qualname__ = "BackingFileSystem"
product_datasets_BackingFileSystem.__module__ = "package_name.product_datasets"


class product_datasets_Dataset(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'rid': ConjureFieldDefinition('rid', str)
        }

    __slots__ = ['_file_system_id', '_rid'] # type: List[str]

    def __init__(self, file_system_id, rid):
        # type: (str, str) -> None
        self._file_system_id = file_system_id
        self._rid = rid

    @builtins.property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @builtins.property
    def rid(self):
        # type: () -> str
        """
        Uniquely identifies this dataset.
        """
        return self._rid


product_datasets_Dataset.__name__ = "Dataset"
product_datasets_Dataset.__qualname__ = "Dataset"
product_datasets_Dataset.__module__ = "package_name.product_datasets"


package_name_TypeInPackageWithTheSameNameAsRootPackage = str

