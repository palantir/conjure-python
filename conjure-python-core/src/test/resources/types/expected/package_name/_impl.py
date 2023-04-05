# coding=utf-8
from abc import (
    ABCMeta,
    abstractmethod,
)
import builtins
from conjure_python_client import (
    BinaryType,
    ConjureBeanType,
    ConjureDecoder,
    ConjureEncoder,
    ConjureEnumType,
    ConjureFieldDefinition,
    ConjureUnionType,
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

    def test_query_params(self, auth_header, implicit, something, list=None, set=None):
        # type: (str, str, str, List[int], List[int]) -> int
        list = list if list is not None else []
        set = set if set is not None else []

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
            'different': something,
            'implicit': implicit,
            'list': list,
            'set': set,
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


class nested_deeply_nested_service_DeeplyNestedService(Service):

    def test_endpoint(self, string):
        # type: (str) -> str

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(string) # type: Any

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str)


nested_deeply_nested_service_DeeplyNestedService.__name__ = "DeeplyNestedService"
nested_deeply_nested_service_DeeplyNestedService.__qualname__ = "DeeplyNestedService"
nested_deeply_nested_service_DeeplyNestedService.__module__ = "package_name.nested_deeply_nested_service"


class nested_service2_SimpleNestedService2(Service):

    def test_endpoint(self, string):
        # type: (str) -> str

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(string) # type: Any

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str)


nested_service2_SimpleNestedService2.__name__ = "SimpleNestedService2"
nested_service2_SimpleNestedService2.__qualname__ = "SimpleNestedService2"
nested_service2_SimpleNestedService2.__module__ = "package_name.nested_service2"


class nested_service_SimpleNestedService(Service):

    def test_endpoint(self, string):
        # type: (str) -> str

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(string) # type: Any

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str)


nested_service_SimpleNestedService.__name__ = "SimpleNestedService"
nested_service_SimpleNestedService.__qualname__ = "SimpleNestedService"
nested_service_SimpleNestedService.__module__ = "package_name.nested_service"


class nested_service_SimpleObject(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str)
        }

    __slots__ = ['_string'] # type: List[str]

    def __init__(self, string):
        # type: (str) -> None
        self._string = string

    @builtins.property
    def string(self):
        # type: () -> str
        return self._string


nested_service_SimpleObject.__name__ = "SimpleObject"
nested_service_SimpleObject.__qualname__ = "SimpleObject"
nested_service_SimpleObject.__module__ = "package_name.nested_service"


class product_AliasAsMapKeyExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'strings': ConjureFieldDefinition('strings', DictType(product_StringAliasExample, product_ManyFieldExample)),
            'rids': ConjureFieldDefinition('rids', DictType(product_RidAliasExample, product_ManyFieldExample)),
            'bearertokens': ConjureFieldDefinition('bearertokens', DictType(product_BearerTokenAliasExample, product_ManyFieldExample)),
            'integers': ConjureFieldDefinition('integers', DictType(product_IntegerAliasExample, product_ManyFieldExample)),
            'safelongs': ConjureFieldDefinition('safelongs', DictType(product_SafeLongAliasExample, product_ManyFieldExample)),
            'datetimes': ConjureFieldDefinition('datetimes', DictType(product_DateTimeAliasExample, product_ManyFieldExample)),
            'uuids': ConjureFieldDefinition('uuids', DictType(product_UuidAliasExample, product_ManyFieldExample))
        }

    __slots__ = ['_strings', '_rids', '_bearertokens', '_integers', '_safelongs', '_datetimes', '_uuids'] # type: List[str]

    def __init__(self, bearertokens, datetimes, integers, rids, safelongs, strings, uuids):
        # type: (Dict[str, product_ManyFieldExample], Dict[str, product_ManyFieldExample], Dict[int, product_ManyFieldExample], Dict[str, product_ManyFieldExample], Dict[int, product_ManyFieldExample], Dict[str, product_ManyFieldExample], Dict[str, product_ManyFieldExample]) -> None
        self._strings = strings
        self._rids = rids
        self._bearertokens = bearertokens
        self._integers = integers
        self._safelongs = safelongs
        self._datetimes = datetimes
        self._uuids = uuids

    @builtins.property
    def strings(self):
        # type: () -> Dict[str, product_ManyFieldExample]
        return self._strings

    @builtins.property
    def rids(self):
        # type: () -> Dict[str, product_ManyFieldExample]
        return self._rids

    @builtins.property
    def bearertokens(self):
        # type: () -> Dict[str, product_ManyFieldExample]
        return self._bearertokens

    @builtins.property
    def integers(self):
        # type: () -> Dict[int, product_ManyFieldExample]
        return self._integers

    @builtins.property
    def safelongs(self):
        # type: () -> Dict[int, product_ManyFieldExample]
        return self._safelongs

    @builtins.property
    def datetimes(self):
        # type: () -> Dict[str, product_ManyFieldExample]
        return self._datetimes

    @builtins.property
    def uuids(self):
        # type: () -> Dict[str, product_ManyFieldExample]
        return self._uuids


product_AliasAsMapKeyExample.__name__ = "AliasAsMapKeyExample"
product_AliasAsMapKeyExample.__qualname__ = "AliasAsMapKeyExample"
product_AliasAsMapKeyExample.__module__ = "package_name.product"


class product_AnyExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'any': ConjureFieldDefinition('any', object)
        }

    __slots__ = ['_any'] # type: List[str]

    def __init__(self, any):
        # type: (Any) -> None
        self._any = any

    @builtins.property
    def any(self):
        # type: () -> Any
        return self._any


product_AnyExample.__name__ = "AnyExample"
product_AnyExample.__qualname__ = "AnyExample"
product_AnyExample.__module__ = "package_name.product"


class product_AnyMapExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', DictType(str, object))
        }

    __slots__ = ['_items'] # type: List[str]

    def __init__(self, items):
        # type: (Dict[str, Any]) -> None
        self._items = items

    @builtins.property
    def items(self):
        # type: () -> Dict[str, Any]
        return self._items


product_AnyMapExample.__name__ = "AnyMapExample"
product_AnyMapExample.__qualname__ = "AnyMapExample"
product_AnyMapExample.__module__ = "package_name.product"


class product_BearerTokenExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'bearer_token_value': ConjureFieldDefinition('bearerTokenValue', str)
        }

    __slots__ = ['_bearer_token_value'] # type: List[str]

    def __init__(self, bearer_token_value):
        # type: (str) -> None
        self._bearer_token_value = bearer_token_value

    @builtins.property
    def bearer_token_value(self):
        # type: () -> str
        return self._bearer_token_value


product_BearerTokenExample.__name__ = "BearerTokenExample"
product_BearerTokenExample.__qualname__ = "BearerTokenExample"
product_BearerTokenExample.__module__ = "package_name.product"


class product_BinaryExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'binary': ConjureFieldDefinition('binary', BinaryType())
        }

    __slots__ = ['_binary'] # type: List[str]

    def __init__(self, binary):
        # type: (Any) -> None
        self._binary = binary

    @builtins.property
    def binary(self):
        # type: () -> Any
        return self._binary


product_BinaryExample.__name__ = "BinaryExample"
product_BinaryExample.__qualname__ = "BinaryExample"
product_BinaryExample.__module__ = "package_name.product"


class product_BooleanExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'coin': ConjureFieldDefinition('coin', bool)
        }

    __slots__ = ['_coin'] # type: List[str]

    def __init__(self, coin):
        # type: (bool) -> None
        self._coin = coin

    @builtins.property
    def coin(self):
        # type: () -> bool
        return self._coin


product_BooleanExample.__name__ = "BooleanExample"
product_BooleanExample.__qualname__ = "BooleanExample"
product_BooleanExample.__module__ = "package_name.product"


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


class product_DateTimeExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'datetime': ConjureFieldDefinition('datetime', str)
        }

    __slots__ = ['_datetime'] # type: List[str]

    def __init__(self, datetime):
        # type: (str) -> None
        self._datetime = datetime

    @builtins.property
    def datetime(self):
        # type: () -> str
        return self._datetime


product_DateTimeExample.__name__ = "DateTimeExample"
product_DateTimeExample.__qualname__ = "DateTimeExample"
product_DateTimeExample.__module__ = "package_name.product"


class product_DoubleExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'double_value': ConjureFieldDefinition('doubleValue', float)
        }

    __slots__ = ['_double_value'] # type: List[str]

    def __init__(self, double_value):
        # type: (float) -> None
        self._double_value = double_value

    @builtins.property
    def double_value(self):
        # type: () -> float
        return self._double_value


product_DoubleExample.__name__ = "DoubleExample"
product_DoubleExample.__qualname__ = "DoubleExample"
product_DoubleExample.__module__ = "package_name.product"


class product_EmptyObjectExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
        }

    __slots__ = [] # type: List[str]



product_EmptyObjectExample.__name__ = "EmptyObjectExample"
product_EmptyObjectExample.__qualname__ = "EmptyObjectExample"
product_EmptyObjectExample.__module__ = "package_name.product"


class product_EnumExample(ConjureEnumType):
    """
    This enumerates the numbers 1:2.
    """

    ONE = 'ONE'
    '''ONE'''
    TWO = 'TWO'
    '''TWO'''
    UNKNOWN = 'UNKNOWN'
    '''UNKNOWN'''

    def __reduce_ex__(self, proto):
        return self.__class__, (self.name,)


product_EnumExample.__name__ = "EnumExample"
product_EnumExample.__qualname__ = "EnumExample"
product_EnumExample.__module__ = "package_name.product"


class product_EnumFieldExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'enum': ConjureFieldDefinition('enum', product_EnumExample)
        }

    __slots__ = ['_enum'] # type: List[str]

    def __init__(self, enum):
        # type: (product_EnumExample) -> None
        self._enum = enum

    @builtins.property
    def enum(self):
        # type: () -> product_EnumExample
        return self._enum


product_EnumFieldExample.__name__ = "EnumFieldExample"
product_EnumFieldExample.__qualname__ = "EnumFieldExample"
product_EnumFieldExample.__module__ = "package_name.product"


class product_FieldObject(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'fields': ConjureFieldDefinition('fields', str)
        }

    __slots__ = ['_fields_'] # type: List[str]

    def __init__(self, fields):
        # type: (str) -> None
        self._fields_ = fields

    @builtins.property
    def fields(self):
        # type: () -> str
        return self._fields_


product_FieldObject.__name__ = "FieldObject"
product_FieldObject.__qualname__ = "FieldObject"
product_FieldObject.__module__ = "package_name.product"


class product_IntegerExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'integer': ConjureFieldDefinition('integer', int)
        }

    __slots__ = ['_integer'] # type: List[str]

    def __init__(self, integer):
        # type: (int) -> None
        self._integer = integer

    @builtins.property
    def integer(self):
        # type: () -> int
        return self._integer


product_IntegerExample.__name__ = "IntegerExample"
product_IntegerExample.__qualname__ = "IntegerExample"
product_IntegerExample.__module__ = "package_name.product"


class product_ListExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', ListType(str)),
            'primitive_items': ConjureFieldDefinition('primitiveItems', ListType(int)),
            'double_items': ConjureFieldDefinition('doubleItems', ListType(float))
        }

    __slots__ = ['_items', '_primitive_items', '_double_items'] # type: List[str]

    def __init__(self, double_items, items, primitive_items):
        # type: (List[float], List[str], List[int]) -> None
        self._items = items
        self._primitive_items = primitive_items
        self._double_items = double_items

    @builtins.property
    def items(self):
        # type: () -> List[str]
        return self._items

    @builtins.property
    def primitive_items(self):
        # type: () -> List[int]
        return self._primitive_items

    @builtins.property
    def double_items(self):
        # type: () -> List[float]
        return self._double_items


product_ListExample.__name__ = "ListExample"
product_ListExample.__qualname__ = "ListExample"
product_ListExample.__module__ = "package_name.product"


class product_ManyFieldExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str),
            'integer': ConjureFieldDefinition('integer', int),
            'double_value': ConjureFieldDefinition('doubleValue', float),
            'optional_item': ConjureFieldDefinition('optionalItem', OptionalType(str)),
            'items': ConjureFieldDefinition('items', ListType(str)),
            'set': ConjureFieldDefinition('set', ListType(str)),
            'map': ConjureFieldDefinition('map', DictType(str, str)),
            'alias': ConjureFieldDefinition('alias', product_StringAliasExample)
        }

    __slots__ = ['_string', '_integer', '_double_value', '_optional_item', '_items', '_set', '_map', '_alias'] # type: List[str]

    def __init__(self, alias, double_value, integer, items, map, set, string, optional_item=None):
        # type: (str, float, int, List[str], Dict[str, str], List[str], str, Optional[str]) -> None
        self._string = string
        self._integer = integer
        self._double_value = double_value
        self._optional_item = optional_item
        self._items = items
        self._set = set
        self._map = map
        self._alias = alias

    @builtins.property
    def string(self):
        # type: () -> str
        """
        docs for string field
        """
        return self._string

    @builtins.property
    def integer(self):
        # type: () -> int
        """
        docs for integer field
        """
        return self._integer

    @builtins.property
    def double_value(self):
        # type: () -> float
        """
        docs for doubleValue field
        """
        return self._double_value

    @builtins.property
    def optional_item(self):
        # type: () -> Optional[str]
        """
        docs for optionalItem field
        """
        return self._optional_item

    @builtins.property
    def items(self):
        # type: () -> List[str]
        """
        docs for items field
        """
        return self._items

    @builtins.property
    def set(self):
        # type: () -> List[str]
        """
        docs for set field
        """
        return self._set

    @builtins.property
    def map(self):
        # type: () -> Dict[str, str]
        """
        docs for map field
        """
        return self._map

    @builtins.property
    def alias(self):
        # type: () -> str
        """
        docs for alias field
        """
        return self._alias


product_ManyFieldExample.__name__ = "ManyFieldExample"
product_ManyFieldExample.__qualname__ = "ManyFieldExample"
product_ManyFieldExample.__module__ = "package_name.product"


class product_MapExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', DictType(str, str))
        }

    __slots__ = ['_items'] # type: List[str]

    def __init__(self, items):
        # type: (Dict[str, str]) -> None
        self._items = items

    @builtins.property
    def items(self):
        # type: () -> Dict[str, str]
        return self._items


product_MapExample.__name__ = "MapExample"
product_MapExample.__qualname__ = "MapExample"
product_MapExample.__module__ = "package_name.product"


class product_OptionalExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'item': ConjureFieldDefinition('item', OptionalType(str))
        }

    __slots__ = ['_item'] # type: List[str]

    def __init__(self, item=None):
        # type: (Optional[str]) -> None
        self._item = item

    @builtins.property
    def item(self):
        # type: () -> Optional[str]
        return self._item


product_OptionalExample.__name__ = "OptionalExample"
product_OptionalExample.__qualname__ = "OptionalExample"
product_OptionalExample.__module__ = "package_name.product"


class product_OptionsUnion(ConjureUnionType):
    _options_ = None # type: Optional[str]

    @builtins.classmethod
    def _options(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'options': ConjureFieldDefinition('options', str)
        }

    def __init__(
            self,
            options=None,  # type: Optional[str]
            type_of_union=None  # type: Optional[str]
            ) -> None:
        if type_of_union is None:
            if (options is not None) != 1:
                raise ValueError('a union must contain a single member')

            if options is not None:
                self._options_ = options
                self._type = 'options'

        elif type_of_union == 'options':
            if options is None:
                raise ValueError('a union value must not be None')
            self._options_ = options
            self._type = 'options'

    @builtins.property
    def options(self):
        # type: () -> Optional[str]
        return self._options_

    def accept(self, visitor):
        # type: (product_OptionsUnionVisitor) -> Any
        if not isinstance(visitor, product_OptionsUnionVisitor):
            raise ValueError('{} is not an instance of product_OptionsUnionVisitor'.format(visitor.__class__.__name__))
        if self._type == 'options' and self.options is not None:
            return visitor._options(self.options)


product_OptionsUnion.__name__ = "OptionsUnion"
product_OptionsUnion.__qualname__ = "OptionsUnion"
product_OptionsUnion.__module__ = "package_name.product"


product_OptionsUnionVisitorBaseClass = ABCMeta('ABC', (object,), {}) # type: Any


class product_OptionsUnionVisitor(product_OptionsUnionVisitorBaseClass):

    @abstractmethod
    def _options(self, options):
        # type: (str) -> Any
        pass


product_OptionsUnionVisitor.__name__ = "OptionsUnionVisitor"
product_OptionsUnionVisitor.__qualname__ = "OptionsUnionVisitor"
product_OptionsUnionVisitor.__module__ = "package_name.product"


class product_PrimitiveOptionalsExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'num': ConjureFieldDefinition('num', OptionalType(float)),
            'bool_': ConjureFieldDefinition('bool', OptionalType(bool)),
            'integer': ConjureFieldDefinition('integer', OptionalType(int)),
            'safelong': ConjureFieldDefinition('safelong', OptionalType(int)),
            'rid': ConjureFieldDefinition('rid', OptionalType(str)),
            'bearertoken': ConjureFieldDefinition('bearertoken', OptionalType(str)),
            'uuid': ConjureFieldDefinition('uuid', OptionalType(str))
        }

    __slots__ = ['_num', '_bool_', '_integer', '_safelong', '_rid', '_bearertoken', '_uuid'] # type: List[str]

    def __init__(self, bearertoken=None, bool_=None, integer=None, num=None, rid=None, safelong=None, uuid=None):
        # type: (Optional[str], Optional[bool], Optional[int], Optional[float], Optional[str], Optional[int], Optional[str]) -> None
        self._num = num
        self._bool_ = bool_
        self._integer = integer
        self._safelong = safelong
        self._rid = rid
        self._bearertoken = bearertoken
        self._uuid = uuid

    @builtins.property
    def num(self):
        # type: () -> Optional[float]
        return self._num

    @builtins.property
    def bool_(self):
        # type: () -> Optional[bool]
        return self._bool_

    @builtins.property
    def integer(self):
        # type: () -> Optional[int]
        return self._integer

    @builtins.property
    def safelong(self):
        # type: () -> Optional[int]
        return self._safelong

    @builtins.property
    def rid(self):
        # type: () -> Optional[str]
        return self._rid

    @builtins.property
    def bearertoken(self):
        # type: () -> Optional[str]
        return self._bearertoken

    @builtins.property
    def uuid(self):
        # type: () -> Optional[str]
        return self._uuid


product_PrimitiveOptionalsExample.__name__ = "PrimitiveOptionalsExample"
product_PrimitiveOptionalsExample.__qualname__ = "PrimitiveOptionalsExample"
product_PrimitiveOptionalsExample.__module__ = "package_name.product"


class product_RecursiveObjectExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'recursive_field': ConjureFieldDefinition('recursiveField', OptionalType(product_RecursiveObjectAlias))
        }

    __slots__ = ['_recursive_field'] # type: List[str]

    def __init__(self, recursive_field=None):
        # type: (Optional[product_RecursiveObjectExample]) -> None
        self._recursive_field = recursive_field

    @builtins.property
    def recursive_field(self):
        # type: () -> Optional[product_RecursiveObjectExample]
        return self._recursive_field


product_RecursiveObjectExample.__name__ = "RecursiveObjectExample"
product_RecursiveObjectExample.__qualname__ = "RecursiveObjectExample"
product_RecursiveObjectExample.__module__ = "package_name.product"


class product_ReservedKeyExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'package': ConjureFieldDefinition('package', str),
            'interface': ConjureFieldDefinition('interface', str),
            'field_name_with_dashes': ConjureFieldDefinition('field-name-with-dashes', str),
            'memoized_hash_code': ConjureFieldDefinition('memoizedHashCode', int)
        }

    __slots__ = ['_package', '_interface', '_field_name_with_dashes', '_memoized_hash_code'] # type: List[str]

    def __init__(self, field_name_with_dashes, interface, memoized_hash_code, package):
        # type: (str, str, int, str) -> None
        self._package = package
        self._interface = interface
        self._field_name_with_dashes = field_name_with_dashes
        self._memoized_hash_code = memoized_hash_code

    @builtins.property
    def package(self):
        # type: () -> str
        return self._package

    @builtins.property
    def interface(self):
        # type: () -> str
        return self._interface

    @builtins.property
    def field_name_with_dashes(self):
        # type: () -> str
        return self._field_name_with_dashes

    @builtins.property
    def memoized_hash_code(self):
        # type: () -> int
        return self._memoized_hash_code


product_ReservedKeyExample.__name__ = "ReservedKeyExample"
product_ReservedKeyExample.__qualname__ = "ReservedKeyExample"
product_ReservedKeyExample.__module__ = "package_name.product"


class product_RidExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'rid_value': ConjureFieldDefinition('ridValue', str)
        }

    __slots__ = ['_rid_value'] # type: List[str]

    def __init__(self, rid_value):
        # type: (str) -> None
        self._rid_value = rid_value

    @builtins.property
    def rid_value(self):
        # type: () -> str
        return self._rid_value


product_RidExample.__name__ = "RidExample"
product_RidExample.__qualname__ = "RidExample"
product_RidExample.__module__ = "package_name.product"


class product_SafeLongExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'safe_long_value': ConjureFieldDefinition('safeLongValue', int)
        }

    __slots__ = ['_safe_long_value'] # type: List[str]

    def __init__(self, safe_long_value):
        # type: (int) -> None
        self._safe_long_value = safe_long_value

    @builtins.property
    def safe_long_value(self):
        # type: () -> int
        return self._safe_long_value


product_SafeLongExample.__name__ = "SafeLongExample"
product_SafeLongExample.__qualname__ = "SafeLongExample"
product_SafeLongExample.__module__ = "package_name.product"


class product_SetExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', ListType(str)),
            'double_items': ConjureFieldDefinition('doubleItems', ListType(float))
        }

    __slots__ = ['_items', '_double_items'] # type: List[str]

    def __init__(self, double_items, items):
        # type: (List[float], List[str]) -> None
        self._items = items
        self._double_items = double_items

    @builtins.property
    def items(self):
        # type: () -> List[str]
        return self._items

    @builtins.property
    def double_items(self):
        # type: () -> List[float]
        return self._double_items


product_SetExample.__name__ = "SetExample"
product_SetExample.__qualname__ = "SetExample"
product_SetExample.__module__ = "package_name.product"


class product_StringExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str)
        }

    __slots__ = ['_string'] # type: List[str]

    def __init__(self, string):
        # type: (str) -> None
        self._string = string

    @builtins.property
    def string(self):
        # type: () -> str
        return self._string


product_StringExample.__name__ = "StringExample"
product_StringExample.__qualname__ = "StringExample"
product_StringExample.__module__ = "package_name.product"


class product_UnionTypeExample(ConjureUnionType):
    """A type which can either be a StringExample, a set of strings, or an integer."""
    _string_example = None # type: Optional[product_StringExample]
    _set = None # type: Optional[List[str]]
    _this_field_is_an_integer = None # type: Optional[int]
    _also_an_integer = None # type: Optional[int]
    _if_ = None # type: Optional[int]
    _new = None # type: Optional[int]
    _interface = None # type: Optional[int]
    _property = None # type: Optional[int]

    @builtins.classmethod
    def _options(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string_example': ConjureFieldDefinition('stringExample', product_StringExample),
            'set': ConjureFieldDefinition('set', ListType(str)),
            'this_field_is_an_integer': ConjureFieldDefinition('thisFieldIsAnInteger', int),
            'also_an_integer': ConjureFieldDefinition('alsoAnInteger', int),
            'if_': ConjureFieldDefinition('if', int),
            'new': ConjureFieldDefinition('new', int),
            'interface': ConjureFieldDefinition('interface', int),
            'property': ConjureFieldDefinition('property', int)
        }

    def __init__(
            self,
            string_example=None,  # type: Optional[product_StringExample]
            set=None,  # type: Optional[List[str]]
            this_field_is_an_integer=None,  # type: Optional[int]
            also_an_integer=None,  # type: Optional[int]
            if_=None,  # type: Optional[int]
            new=None,  # type: Optional[int]
            interface=None,  # type: Optional[int]
            property=None,  # type: Optional[int]
            type_of_union=None  # type: Optional[str]
            ) -> None:
        if type_of_union is None:
            if (string_example is not None) + (set is not None) + (this_field_is_an_integer is not None) + (also_an_integer is not None) + (if_ is not None) + (new is not None) + (interface is not None) + (property is not None) != 1:
                raise ValueError('a union must contain a single member')

            if string_example is not None:
                self._string_example = string_example
                self._type = 'stringExample'
            if set is not None:
                self._set = set
                self._type = 'set'
            if this_field_is_an_integer is not None:
                self._this_field_is_an_integer = this_field_is_an_integer
                self._type = 'thisFieldIsAnInteger'
            if also_an_integer is not None:
                self._also_an_integer = also_an_integer
                self._type = 'alsoAnInteger'
            if if_ is not None:
                self._if_ = if_
                self._type = 'if'
            if new is not None:
                self._new = new
                self._type = 'new'
            if interface is not None:
                self._interface = interface
                self._type = 'interface'
            if property is not None:
                self._property = property
                self._type = 'property'

        elif type_of_union == 'stringExample':
            if string_example is None:
                raise ValueError('a union value must not be None')
            self._string_example = string_example
            self._type = 'stringExample'
        elif type_of_union == 'set':
            if set is None:
                raise ValueError('a union value must not be None')
            self._set = set
            self._type = 'set'
        elif type_of_union == 'thisFieldIsAnInteger':
            if this_field_is_an_integer is None:
                raise ValueError('a union value must not be None')
            self._this_field_is_an_integer = this_field_is_an_integer
            self._type = 'thisFieldIsAnInteger'
        elif type_of_union == 'alsoAnInteger':
            if also_an_integer is None:
                raise ValueError('a union value must not be None')
            self._also_an_integer = also_an_integer
            self._type = 'alsoAnInteger'
        elif type_of_union == 'if':
            if if_ is None:
                raise ValueError('a union value must not be None')
            self._if_ = if_
            self._type = 'if'
        elif type_of_union == 'new':
            if new is None:
                raise ValueError('a union value must not be None')
            self._new = new
            self._type = 'new'
        elif type_of_union == 'interface':
            if interface is None:
                raise ValueError('a union value must not be None')
            self._interface = interface
            self._type = 'interface'
        elif type_of_union == 'property':
            if property is None:
                raise ValueError('a union value must not be None')
            self._property = property
            self._type = 'property'

    @builtins.property
    def string_example(self):
        # type: () -> Optional[product_StringExample]
        """
        Docs for when UnionTypeExample is of type StringExample.
        """
        return self._string_example

    @builtins.property
    def set(self):
        # type: () -> Optional[List[str]]
        return self._set

    @builtins.property
    def this_field_is_an_integer(self):
        # type: () -> Optional[int]
        return self._this_field_is_an_integer

    @builtins.property
    def also_an_integer(self):
        # type: () -> Optional[int]
        return self._also_an_integer

    @builtins.property
    def if_(self):
        # type: () -> Optional[int]
        return self._if_

    @builtins.property
    def new(self):
        # type: () -> Optional[int]
        return self._new

    @builtins.property
    def interface(self):
        # type: () -> Optional[int]
        return self._interface

    @builtins.property
    def property(self):
        # type: () -> Optional[int]
        return self._property

    def accept(self, visitor):
        # type: (product_UnionTypeExampleVisitor) -> Any
        if not isinstance(visitor, product_UnionTypeExampleVisitor):
            raise ValueError('{} is not an instance of product_UnionTypeExampleVisitor'.format(visitor.__class__.__name__))
        if self._type == 'stringExample' and self.string_example is not None:
            return visitor._string_example(self.string_example)
        if self._type == 'set' and self.set is not None:
            return visitor._set(self.set)
        if self._type == 'thisFieldIsAnInteger' and self.this_field_is_an_integer is not None:
            return visitor._this_field_is_an_integer(self.this_field_is_an_integer)
        if self._type == 'alsoAnInteger' and self.also_an_integer is not None:
            return visitor._also_an_integer(self.also_an_integer)
        if self._type == 'if' and self.if_ is not None:
            return visitor._if(self.if_)
        if self._type == 'new' and self.new is not None:
            return visitor._new(self.new)
        if self._type == 'interface' and self.interface is not None:
            return visitor._interface(self.interface)
        if self._type == 'property' and self.property is not None:
            return visitor._property(self.property)


product_UnionTypeExample.__name__ = "UnionTypeExample"
product_UnionTypeExample.__qualname__ = "UnionTypeExample"
product_UnionTypeExample.__module__ = "package_name.product"


product_UnionTypeExampleVisitorBaseClass = ABCMeta('ABC', (object,), {}) # type: Any


class product_UnionTypeExampleVisitor(product_UnionTypeExampleVisitorBaseClass):

    @abstractmethod
    def _string_example(self, string_example):
        # type: (product_StringExample) -> Any
        pass

    @abstractmethod
    def _set(self, set):
        # type: (List[str]) -> Any
        pass

    @abstractmethod
    def _this_field_is_an_integer(self, this_field_is_an_integer):
        # type: (int) -> Any
        pass

    @abstractmethod
    def _also_an_integer(self, also_an_integer):
        # type: (int) -> Any
        pass

    @abstractmethod
    def _if(self, if_):
        # type: (int) -> Any
        pass

    @abstractmethod
    def _new(self, new):
        # type: (int) -> Any
        pass

    @abstractmethod
    def _interface(self, interface):
        # type: (int) -> Any
        pass

    @abstractmethod
    def _property(self, property):
        # type: (int) -> Any
        pass


product_UnionTypeExampleVisitor.__name__ = "UnionTypeExampleVisitor"
product_UnionTypeExampleVisitor.__qualname__ = "UnionTypeExampleVisitor"
product_UnionTypeExampleVisitor.__module__ = "package_name.product"


class product_UuidExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'uuid': ConjureFieldDefinition('uuid', str)
        }

    __slots__ = ['_uuid'] # type: List[str]

    def __init__(self, uuid):
        # type: (str) -> None
        self._uuid = uuid

    @builtins.property
    def uuid(self):
        # type: () -> str
        return self._uuid


product_UuidExample.__name__ = "UuidExample"
product_UuidExample.__qualname__ = "UuidExample"
product_UuidExample.__module__ = "package_name.product"


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


class with_imports_ComplexObjectWithImports(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', product_StringExample)
        }

    __slots__ = ['_string', '_imported'] # type: List[str]

    def __init__(self, imported, string):
        # type: (product_StringExample, str) -> None
        self._string = string
        self._imported = imported

    @builtins.property
    def string(self):
        # type: () -> str
        return self._string

    @builtins.property
    def imported(self):
        # type: () -> product_StringExample
        return self._imported


with_imports_ComplexObjectWithImports.__name__ = "ComplexObjectWithImports"
with_imports_ComplexObjectWithImports.__qualname__ = "ComplexObjectWithImports"
with_imports_ComplexObjectWithImports.__module__ = "package_name.with_imports"


class with_imports_ImportService(Service):

    def test_endpoint(self, imported_string):
        # type: (product_StringExample) -> product_datasets_BackingFileSystem

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(imported_string) # type: Any

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), product_datasets_BackingFileSystem)


with_imports_ImportService.__name__ = "ImportService"
with_imports_ImportService.__qualname__ = "ImportService"
with_imports_ImportService.__module__ = "package_name.with_imports"


class with_imports_ImportedAliasInMaps(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'aliases': ConjureFieldDefinition('aliases', DictType(product_RidAliasExample, product_DateTimeAliasExample))
        }

    __slots__ = ['_aliases'] # type: List[str]

    def __init__(self, aliases):
        # type: (Dict[str, str]) -> None
        self._aliases = aliases

    @builtins.property
    def aliases(self):
        # type: () -> Dict[str, str]
        return self._aliases


with_imports_ImportedAliasInMaps.__name__ = "ImportedAliasInMaps"
with_imports_ImportedAliasInMaps.__qualname__ = "ImportedAliasInMaps"
with_imports_ImportedAliasInMaps.__module__ = "package_name.with_imports"


class with_imports_UnionWithImports(ConjureUnionType):
    _string = None # type: Optional[str]
    _imported = None # type: Optional[product_AnyMapExample]

    @builtins.classmethod
    def _options(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', product_AnyMapExample)
        }

    def __init__(
            self,
            string=None,  # type: Optional[str]
            imported=None,  # type: Optional[product_AnyMapExample]
            type_of_union=None  # type: Optional[str]
            ) -> None:
        if type_of_union is None:
            if (string is not None) + (imported is not None) != 1:
                raise ValueError('a union must contain a single member')

            if string is not None:
                self._string = string
                self._type = 'string'
            if imported is not None:
                self._imported = imported
                self._type = 'imported'

        elif type_of_union == 'string':
            if string is None:
                raise ValueError('a union value must not be None')
            self._string = string
            self._type = 'string'
        elif type_of_union == 'imported':
            if imported is None:
                raise ValueError('a union value must not be None')
            self._imported = imported
            self._type = 'imported'

    @builtins.property
    def string(self):
        # type: () -> Optional[str]
        return self._string

    @builtins.property
    def imported(self):
        # type: () -> Optional[product_AnyMapExample]
        return self._imported

    def accept(self, visitor):
        # type: (with_imports_UnionWithImportsVisitor) -> Any
        if not isinstance(visitor, with_imports_UnionWithImportsVisitor):
            raise ValueError('{} is not an instance of with_imports_UnionWithImportsVisitor'.format(visitor.__class__.__name__))
        if self._type == 'string' and self.string is not None:
            return visitor._string(self.string)
        if self._type == 'imported' and self.imported is not None:
            return visitor._imported(self.imported)


with_imports_UnionWithImports.__name__ = "UnionWithImports"
with_imports_UnionWithImports.__qualname__ = "UnionWithImports"
with_imports_UnionWithImports.__module__ = "package_name.with_imports"


with_imports_UnionWithImportsVisitorBaseClass = ABCMeta('ABC', (object,), {}) # type: Any


class with_imports_UnionWithImportsVisitor(with_imports_UnionWithImportsVisitorBaseClass):

    @abstractmethod
    def _string(self, string):
        # type: (str) -> Any
        pass

    @abstractmethod
    def _imported(self, imported):
        # type: (product_AnyMapExample) -> Any
        pass


with_imports_UnionWithImportsVisitor.__name__ = "UnionWithImportsVisitor"
with_imports_UnionWithImportsVisitor.__qualname__ = "UnionWithImportsVisitor"
with_imports_UnionWithImportsVisitor.__module__ = "package_name.with_imports"


product_SafeLongAliasExample = int

product_BearerTokenAliasExample = str

product_MapAliasExample = DictType(str, object)

product_ReferenceAliasExample = product_AnyExample

product_BooleanAliasExample = bool

product_StringAliasExample = str

product_IntegerAliasExample = int

product_UuidAliasExample = str

with_imports_AliasImportedPrimitiveAlias = product_StringAliasExample

product_DoubleAliasExample = float

product_RidAliasExample = str

package_name_TypeInPackageWithTheSameNameAsRootPackage = str

product_DateTimeAliasExample = str

product_RecursiveObjectAlias = product_RecursiveObjectExample

product_CollectionAliasExample = DictType(product_StringAliasExample, product_RecursiveObjectAlias)

product_NestedAliasExample = product_RecursiveObjectAlias

with_imports_AliasImportedObject = product_ManyFieldExample

product_BinaryAliasExample = BinaryType()

with_imports_AliasImportedReferenceAlias = product_ReferenceAliasExample

