# coding=utf-8
from abc import (
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


class nested_deeply_nested_service_DeeplyNestedService(Service):

    def test_endpoint(self, string: str) -> str:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = ConjureEncoder().default(string)

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str, self._return_none_for_unknown_union_types)


nested_deeply_nested_service_DeeplyNestedService.__name__ = "DeeplyNestedService"
nested_deeply_nested_service_DeeplyNestedService.__qualname__ = "DeeplyNestedService"
nested_deeply_nested_service_DeeplyNestedService.__module__ = "package_name.nested_deeply_nested_service"


class nested_service2_SimpleNestedService2(Service):

    def test_endpoint(self, string: str) -> str:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = ConjureEncoder().default(string)

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str, self._return_none_for_unknown_union_types)


nested_service2_SimpleNestedService2.__name__ = "SimpleNestedService2"
nested_service2_SimpleNestedService2.__qualname__ = "SimpleNestedService2"
nested_service2_SimpleNestedService2.__module__ = "package_name.nested_service2"


class nested_service_SimpleNestedService(Service):

    def test_endpoint(self, string: str) -> str:

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = ConjureEncoder().default(string)

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str, self._return_none_for_unknown_union_types)


nested_service_SimpleNestedService.__name__ = "SimpleNestedService"
nested_service_SimpleNestedService.__qualname__ = "SimpleNestedService"
nested_service_SimpleNestedService.__module__ = "package_name.nested_service"


class nested_service_SimpleObject(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'string': ConjureFieldDefinition('string', str)
        }

    __slots__: List[str] = ['_string']

    def __init__(self, string: str) -> None:
        self._string = string

    @builtins.property
    def string(self) -> str:
        return self._string


nested_service_SimpleObject.__name__ = "SimpleObject"
nested_service_SimpleObject.__qualname__ = "SimpleObject"
nested_service_SimpleObject.__module__ = "package_name.nested_service"


class product_AliasAsMapKeyExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'strings': ConjureFieldDefinition('strings', Dict[product_StringAliasExample, product_ManyFieldExample]),
            'rids': ConjureFieldDefinition('rids', Dict[product_RidAliasExample, product_ManyFieldExample]),
            'bearertokens': ConjureFieldDefinition('bearertokens', Dict[product_BearerTokenAliasExample, product_ManyFieldExample]),
            'integers': ConjureFieldDefinition('integers', Dict[product_IntegerAliasExample, product_ManyFieldExample]),
            'safelongs': ConjureFieldDefinition('safelongs', Dict[product_SafeLongAliasExample, product_ManyFieldExample]),
            'datetimes': ConjureFieldDefinition('datetimes', Dict[product_DateTimeAliasExample, product_ManyFieldExample]),
            'uuids': ConjureFieldDefinition('uuids', Dict[product_UuidAliasExample, product_ManyFieldExample])
        }

    __slots__: List[str] = ['_strings', '_rids', '_bearertokens', '_integers', '_safelongs', '_datetimes', '_uuids']

    def __init__(self, bearertokens: Dict[str, "product_ManyFieldExample"], datetimes: Dict[str, "product_ManyFieldExample"], integers: Dict[int, "product_ManyFieldExample"], rids: Dict[str, "product_ManyFieldExample"], safelongs: Dict[int, "product_ManyFieldExample"], strings: Dict[str, "product_ManyFieldExample"], uuids: Dict[str, "product_ManyFieldExample"]) -> None:
        self._strings = strings
        self._rids = rids
        self._bearertokens = bearertokens
        self._integers = integers
        self._safelongs = safelongs
        self._datetimes = datetimes
        self._uuids = uuids

    @builtins.property
    def strings(self) -> Dict[str, "product_ManyFieldExample"]:
        return self._strings

    @builtins.property
    def rids(self) -> Dict[str, "product_ManyFieldExample"]:
        return self._rids

    @builtins.property
    def bearertokens(self) -> Dict[str, "product_ManyFieldExample"]:
        return self._bearertokens

    @builtins.property
    def integers(self) -> Dict[int, "product_ManyFieldExample"]:
        return self._integers

    @builtins.property
    def safelongs(self) -> Dict[int, "product_ManyFieldExample"]:
        return self._safelongs

    @builtins.property
    def datetimes(self) -> Dict[str, "product_ManyFieldExample"]:
        return self._datetimes

    @builtins.property
    def uuids(self) -> Dict[str, "product_ManyFieldExample"]:
        return self._uuids


product_AliasAsMapKeyExample.__name__ = "AliasAsMapKeyExample"
product_AliasAsMapKeyExample.__qualname__ = "AliasAsMapKeyExample"
product_AliasAsMapKeyExample.__module__ = "package_name.product"


class product_AnyExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'any': ConjureFieldDefinition('any', object)
        }

    __slots__: List[str] = ['_any']

    def __init__(self, any: Any) -> None:
        self._any = any

    @builtins.property
    def any(self) -> Any:
        return self._any


product_AnyExample.__name__ = "AnyExample"
product_AnyExample.__qualname__ = "AnyExample"
product_AnyExample.__module__ = "package_name.product"


class product_AnyMapExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'items': ConjureFieldDefinition('items', Dict[str, object])
        }

    __slots__: List[str] = ['_items']

    def __init__(self, items: Dict[str, Any]) -> None:
        self._items = items

    @builtins.property
    def items(self) -> Dict[str, Any]:
        return self._items


product_AnyMapExample.__name__ = "AnyMapExample"
product_AnyMapExample.__qualname__ = "AnyMapExample"
product_AnyMapExample.__module__ = "package_name.product"


class product_BearerTokenExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'bearer_token_value': ConjureFieldDefinition('bearerTokenValue', str)
        }

    __slots__: List[str] = ['_bearer_token_value']

    def __init__(self, bearer_token_value: str) -> None:
        self._bearer_token_value = bearer_token_value

    @builtins.property
    def bearer_token_value(self) -> str:
        return self._bearer_token_value


product_BearerTokenExample.__name__ = "BearerTokenExample"
product_BearerTokenExample.__qualname__ = "BearerTokenExample"
product_BearerTokenExample.__module__ = "package_name.product"


class product_BinaryExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'binary': ConjureFieldDefinition('binary', BinaryType)
        }

    __slots__: List[str] = ['_binary']

    def __init__(self, binary: Any) -> None:
        self._binary = binary

    @builtins.property
    def binary(self) -> Any:
        return self._binary


product_BinaryExample.__name__ = "BinaryExample"
product_BinaryExample.__qualname__ = "BinaryExample"
product_BinaryExample.__module__ = "package_name.product"


class product_BooleanExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'coin': ConjureFieldDefinition('coin', bool)
        }

    __slots__: List[str] = ['_coin']

    def __init__(self, coin: bool) -> None:
        self._coin = coin

    @builtins.property
    def coin(self) -> bool:
        return self._coin


product_BooleanExample.__name__ = "BooleanExample"
product_BooleanExample.__qualname__ = "BooleanExample"
product_BooleanExample.__module__ = "package_name.product"


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


class product_DateTimeExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'datetime': ConjureFieldDefinition('datetime', str)
        }

    __slots__: List[str] = ['_datetime']

    def __init__(self, datetime: str) -> None:
        self._datetime = datetime

    @builtins.property
    def datetime(self) -> str:
        return self._datetime


product_DateTimeExample.__name__ = "DateTimeExample"
product_DateTimeExample.__qualname__ = "DateTimeExample"
product_DateTimeExample.__module__ = "package_name.product"


class product_DoubleExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'double_value': ConjureFieldDefinition('doubleValue', float)
        }

    __slots__: List[str] = ['_double_value']

    def __init__(self, double_value: float) -> None:
        self._double_value = double_value

    @builtins.property
    def double_value(self) -> float:
        return self._double_value


product_DoubleExample.__name__ = "DoubleExample"
product_DoubleExample.__qualname__ = "DoubleExample"
product_DoubleExample.__module__ = "package_name.product"


class product_EmptyObjectExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
        }

    __slots__: List[str] = []



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
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'enum': ConjureFieldDefinition('enum', product_EnumExample)
        }

    __slots__: List[str] = ['_enum']

    def __init__(self, enum: "product_EnumExample") -> None:
        self._enum = enum

    @builtins.property
    def enum(self) -> "product_EnumExample":
        return self._enum


product_EnumFieldExample.__name__ = "EnumFieldExample"
product_EnumFieldExample.__qualname__ = "EnumFieldExample"
product_EnumFieldExample.__module__ = "package_name.product"


class product_FieldObject(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'fields': ConjureFieldDefinition('fields', str)
        }

    __slots__: List[str] = ['_fields_']

    def __init__(self, fields: str) -> None:
        self._fields_ = fields

    @builtins.property
    def fields(self) -> str:
        return self._fields_


product_FieldObject.__name__ = "FieldObject"
product_FieldObject.__qualname__ = "FieldObject"
product_FieldObject.__module__ = "package_name.product"


class product_IntegerExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'integer': ConjureFieldDefinition('integer', int)
        }

    __slots__: List[str] = ['_integer']

    def __init__(self, integer: int) -> None:
        self._integer = integer

    @builtins.property
    def integer(self) -> int:
        return self._integer


product_IntegerExample.__name__ = "IntegerExample"
product_IntegerExample.__qualname__ = "IntegerExample"
product_IntegerExample.__module__ = "package_name.product"


class product_ListExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'items': ConjureFieldDefinition('items', List[str]),
            'primitive_items': ConjureFieldDefinition('primitiveItems', List[int]),
            'double_items': ConjureFieldDefinition('doubleItems', List[float])
        }

    __slots__: List[str] = ['_items', '_primitive_items', '_double_items']

    def __init__(self, double_items: List[float], items: List[str], primitive_items: List[int]) -> None:
        self._items = items
        self._primitive_items = primitive_items
        self._double_items = double_items

    @builtins.property
    def items(self) -> List[str]:
        return self._items

    @builtins.property
    def primitive_items(self) -> List[int]:
        return self._primitive_items

    @builtins.property
    def double_items(self) -> List[float]:
        return self._double_items


product_ListExample.__name__ = "ListExample"
product_ListExample.__qualname__ = "ListExample"
product_ListExample.__module__ = "package_name.product"


class product_ManyFieldExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'string': ConjureFieldDefinition('string', str),
            'integer': ConjureFieldDefinition('integer', int),
            'double_value': ConjureFieldDefinition('doubleValue', float),
            'optional_item': ConjureFieldDefinition('optionalItem', OptionalTypeWrapper[str]),
            'items': ConjureFieldDefinition('items', List[str]),
            'set': ConjureFieldDefinition('set', List[str]),
            'map': ConjureFieldDefinition('map', Dict[str, str]),
            'alias': ConjureFieldDefinition('alias', product_StringAliasExample)
        }

    __slots__: List[str] = ['_string', '_integer', '_double_value', '_optional_item', '_items', '_set', '_map', '_alias']

    def __init__(self, alias: str, double_value: float, integer: int, items: List[str], map: Dict[str, str], set: List[str], string: str, optional_item: Optional[str] = None) -> None:
        self._string = string
        self._integer = integer
        self._double_value = double_value
        self._optional_item = optional_item
        self._items = items
        self._set = set
        self._map = map
        self._alias = alias

    @builtins.property
    def string(self) -> str:
        """
        docs for string field
        """
        return self._string

    @builtins.property
    def integer(self) -> int:
        """
        docs for integer field
        """
        return self._integer

    @builtins.property
    def double_value(self) -> float:
        """
        docs for doubleValue field
        """
        return self._double_value

    @builtins.property
    def optional_item(self) -> Optional[str]:
        """
        docs for optionalItem field
        """
        return self._optional_item

    @builtins.property
    def items(self) -> List[str]:
        """
        docs for items field
        """
        return self._items

    @builtins.property
    def set(self) -> List[str]:
        """
        docs for set field
        """
        return self._set

    @builtins.property
    def map(self) -> Dict[str, str]:
        """
        docs for map field
        """
        return self._map

    @builtins.property
    def alias(self) -> str:
        """
        docs for alias field
        """
        return self._alias


product_ManyFieldExample.__name__ = "ManyFieldExample"
product_ManyFieldExample.__qualname__ = "ManyFieldExample"
product_ManyFieldExample.__module__ = "package_name.product"


class product_MapExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'items': ConjureFieldDefinition('items', Dict[str, str])
        }

    __slots__: List[str] = ['_items']

    def __init__(self, items: Dict[str, str]) -> None:
        self._items = items

    @builtins.property
    def items(self) -> Dict[str, str]:
        return self._items


product_MapExample.__name__ = "MapExample"
product_MapExample.__qualname__ = "MapExample"
product_MapExample.__module__ = "package_name.product"


class product_OptionalExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'item': ConjureFieldDefinition('item', OptionalTypeWrapper[str])
        }

    __slots__: List[str] = ['_item']

    def __init__(self, item: Optional[str] = None) -> None:
        self._item = item

    @builtins.property
    def item(self) -> Optional[str]:
        return self._item


product_OptionalExample.__name__ = "OptionalExample"
product_OptionalExample.__qualname__ = "OptionalExample"
product_OptionalExample.__module__ = "package_name.product"


class product_OptionsUnion(ConjureUnionType):
    _options_: Optional[str] = None

    @builtins.classmethod
    def _options(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'options': ConjureFieldDefinition('options', str)
        }

    def __init__(
            self,
            options: Optional[str] = None,
            type_of_union: Optional[str] = None
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
    def options(self) -> Optional[str]:
        return self._options_

    def accept(self, visitor) -> Any:
        if not isinstance(visitor, product_OptionsUnionVisitor):
            raise ValueError('{} is not an instance of product_OptionsUnionVisitor'.format(visitor.__class__.__name__))
        if self._type == 'options' and self.options is not None:
            return visitor._options(self.options)


product_OptionsUnion.__name__ = "OptionsUnion"
product_OptionsUnion.__qualname__ = "OptionsUnion"
product_OptionsUnion.__module__ = "package_name.product"


class product_OptionsUnionVisitor:

    @abstractmethod
    def _options(self, options: str) -> Any:
        pass


product_OptionsUnionVisitor.__name__ = "OptionsUnionVisitor"
product_OptionsUnionVisitor.__qualname__ = "OptionsUnionVisitor"
product_OptionsUnionVisitor.__module__ = "package_name.product"


class product_PrimitiveOptionalsExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'num': ConjureFieldDefinition('num', OptionalTypeWrapper[float]),
            'bool_': ConjureFieldDefinition('bool', OptionalTypeWrapper[bool]),
            'integer': ConjureFieldDefinition('integer', OptionalTypeWrapper[int]),
            'safelong': ConjureFieldDefinition('safelong', OptionalTypeWrapper[int]),
            'rid': ConjureFieldDefinition('rid', OptionalTypeWrapper[str]),
            'bearertoken': ConjureFieldDefinition('bearertoken', OptionalTypeWrapper[str]),
            'uuid': ConjureFieldDefinition('uuid', OptionalTypeWrapper[str])
        }

    __slots__: List[str] = ['_num', '_bool_', '_integer', '_safelong', '_rid', '_bearertoken', '_uuid']

    def __init__(self, bearertoken: Optional[str] = None, bool_: Optional[bool] = None, integer: Optional[int] = None, num: Optional[float] = None, rid: Optional[str] = None, safelong: Optional[int] = None, uuid: Optional[str] = None) -> None:
        self._num = num
        self._bool_ = bool_
        self._integer = integer
        self._safelong = safelong
        self._rid = rid
        self._bearertoken = bearertoken
        self._uuid = uuid

    @builtins.property
    def num(self) -> Optional[float]:
        return self._num

    @builtins.property
    def bool_(self) -> Optional[bool]:
        return self._bool_

    @builtins.property
    def integer(self) -> Optional[int]:
        return self._integer

    @builtins.property
    def safelong(self) -> Optional[int]:
        return self._safelong

    @builtins.property
    def rid(self) -> Optional[str]:
        return self._rid

    @builtins.property
    def bearertoken(self) -> Optional[str]:
        return self._bearertoken

    @builtins.property
    def uuid(self) -> Optional[str]:
        return self._uuid


product_PrimitiveOptionalsExample.__name__ = "PrimitiveOptionalsExample"
product_PrimitiveOptionalsExample.__qualname__ = "PrimitiveOptionalsExample"
product_PrimitiveOptionalsExample.__module__ = "package_name.product"


class product_RecursiveObjectExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'recursive_field': ConjureFieldDefinition('recursiveField', OptionalTypeWrapper[product_RecursiveObjectAlias])
        }

    __slots__: List[str] = ['_recursive_field']

    def __init__(self, recursive_field: Optional["product_RecursiveObjectExample"] = None) -> None:
        self._recursive_field = recursive_field

    @builtins.property
    def recursive_field(self) -> Optional["product_RecursiveObjectExample"]:
        return self._recursive_field


product_RecursiveObjectExample.__name__ = "RecursiveObjectExample"
product_RecursiveObjectExample.__qualname__ = "RecursiveObjectExample"
product_RecursiveObjectExample.__module__ = "package_name.product"


class product_ReservedKeyExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'package': ConjureFieldDefinition('package', str),
            'interface': ConjureFieldDefinition('interface', str),
            'field_name_with_dashes': ConjureFieldDefinition('field-name-with-dashes', str),
            'memoized_hash_code': ConjureFieldDefinition('memoizedHashCode', int)
        }

    __slots__: List[str] = ['_package', '_interface', '_field_name_with_dashes', '_memoized_hash_code']

    def __init__(self, field_name_with_dashes: str, interface: str, memoized_hash_code: int, package: str) -> None:
        self._package = package
        self._interface = interface
        self._field_name_with_dashes = field_name_with_dashes
        self._memoized_hash_code = memoized_hash_code

    @builtins.property
    def package(self) -> str:
        return self._package

    @builtins.property
    def interface(self) -> str:
        return self._interface

    @builtins.property
    def field_name_with_dashes(self) -> str:
        return self._field_name_with_dashes

    @builtins.property
    def memoized_hash_code(self) -> int:
        return self._memoized_hash_code


product_ReservedKeyExample.__name__ = "ReservedKeyExample"
product_ReservedKeyExample.__qualname__ = "ReservedKeyExample"
product_ReservedKeyExample.__module__ = "package_name.product"


class product_RidExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'rid_value': ConjureFieldDefinition('ridValue', str)
        }

    __slots__: List[str] = ['_rid_value']

    def __init__(self, rid_value: str) -> None:
        self._rid_value = rid_value

    @builtins.property
    def rid_value(self) -> str:
        return self._rid_value


product_RidExample.__name__ = "RidExample"
product_RidExample.__qualname__ = "RidExample"
product_RidExample.__module__ = "package_name.product"


class product_SafeLongExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'safe_long_value': ConjureFieldDefinition('safeLongValue', int)
        }

    __slots__: List[str] = ['_safe_long_value']

    def __init__(self, safe_long_value: int) -> None:
        self._safe_long_value = safe_long_value

    @builtins.property
    def safe_long_value(self) -> int:
        return self._safe_long_value


product_SafeLongExample.__name__ = "SafeLongExample"
product_SafeLongExample.__qualname__ = "SafeLongExample"
product_SafeLongExample.__module__ = "package_name.product"


class product_SetExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'items': ConjureFieldDefinition('items', List[str]),
            'double_items': ConjureFieldDefinition('doubleItems', List[float])
        }

    __slots__: List[str] = ['_items', '_double_items']

    def __init__(self, double_items: List[float], items: List[str]) -> None:
        self._items = items
        self._double_items = double_items

    @builtins.property
    def items(self) -> List[str]:
        return self._items

    @builtins.property
    def double_items(self) -> List[float]:
        return self._double_items


product_SetExample.__name__ = "SetExample"
product_SetExample.__qualname__ = "SetExample"
product_SetExample.__module__ = "package_name.product"


class product_StringExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'string': ConjureFieldDefinition('string', str)
        }

    __slots__: List[str] = ['_string']

    def __init__(self, string: str) -> None:
        self._string = string

    @builtins.property
    def string(self) -> str:
        return self._string


product_StringExample.__name__ = "StringExample"
product_StringExample.__qualname__ = "StringExample"
product_StringExample.__module__ = "package_name.product"


class product_UnionTypeExample(ConjureUnionType):
    """A type which can either be a StringExample, a set of strings, or an integer."""
    _string_example: Optional["product_StringExample"] = None
    _set: Optional[List[str]] = None
    _this_field_is_an_integer: Optional[int] = None
    _also_an_integer: Optional[int] = None
    _if_: Optional[int] = None
    _new: Optional[int] = None
    _interface: Optional[int] = None
    _property: Optional[int] = None

    @builtins.classmethod
    def _options(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'string_example': ConjureFieldDefinition('stringExample', product_StringExample),
            'set': ConjureFieldDefinition('set', List[str]),
            'this_field_is_an_integer': ConjureFieldDefinition('thisFieldIsAnInteger', int),
            'also_an_integer': ConjureFieldDefinition('alsoAnInteger', int),
            'if_': ConjureFieldDefinition('if', int),
            'new': ConjureFieldDefinition('new', int),
            'interface': ConjureFieldDefinition('interface', int),
            'property': ConjureFieldDefinition('property', int)
        }

    def __init__(
            self,
            string_example: Optional["product_StringExample"] = None,
            set: Optional[List[str]] = None,
            this_field_is_an_integer: Optional[int] = None,
            also_an_integer: Optional[int] = None,
            if_: Optional[int] = None,
            new: Optional[int] = None,
            interface: Optional[int] = None,
            property: Optional[int] = None,
            type_of_union: Optional[str] = None
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
    def string_example(self) -> Optional["product_StringExample"]:
        """
        Docs for when UnionTypeExample is of type StringExample.
        """
        return self._string_example

    @builtins.property
    def set(self) -> Optional[List[str]]:
        return self._set

    @builtins.property
    def this_field_is_an_integer(self) -> Optional[int]:
        return self._this_field_is_an_integer

    @builtins.property
    def also_an_integer(self) -> Optional[int]:
        return self._also_an_integer

    @builtins.property
    def if_(self) -> Optional[int]:
        return self._if_

    @builtins.property
    def new(self) -> Optional[int]:
        return self._new

    @builtins.property
    def interface(self) -> Optional[int]:
        return self._interface

    @builtins.property
    def property(self) -> Optional[int]:
        return self._property

    def accept(self, visitor) -> Any:
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


class product_UnionTypeExampleVisitor:

    @abstractmethod
    def _string_example(self, string_example: "product_StringExample") -> Any:
        pass

    @abstractmethod
    def _set(self, set: List[str]) -> Any:
        pass

    @abstractmethod
    def _this_field_is_an_integer(self, this_field_is_an_integer: int) -> Any:
        pass

    @abstractmethod
    def _also_an_integer(self, also_an_integer: int) -> Any:
        pass

    @abstractmethod
    def _if(self, if_: int) -> Any:
        pass

    @abstractmethod
    def _new(self, new: int) -> Any:
        pass

    @abstractmethod
    def _interface(self, interface: int) -> Any:
        pass

    @abstractmethod
    def _property(self, property: int) -> Any:
        pass


product_UnionTypeExampleVisitor.__name__ = "UnionTypeExampleVisitor"
product_UnionTypeExampleVisitor.__qualname__ = "UnionTypeExampleVisitor"
product_UnionTypeExampleVisitor.__module__ = "package_name.product"


class product_UuidExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'uuid': ConjureFieldDefinition('uuid', str)
        }

    __slots__: List[str] = ['_uuid']

    def __init__(self, uuid: str) -> None:
        self._uuid = uuid

    @builtins.property
    def uuid(self) -> str:
        return self._uuid


product_UuidExample.__name__ = "UuidExample"
product_UuidExample.__qualname__ = "UuidExample"
product_UuidExample.__module__ = "package_name.product"


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


class with_imports_ComplexObjectWithImports(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', product_StringExample)
        }

    __slots__: List[str] = ['_string', '_imported']

    def __init__(self, imported: "product_StringExample", string: str) -> None:
        self._string = string
        self._imported = imported

    @builtins.property
    def string(self) -> str:
        return self._string

    @builtins.property
    def imported(self) -> "product_StringExample":
        return self._imported


with_imports_ComplexObjectWithImports.__name__ = "ComplexObjectWithImports"
with_imports_ComplexObjectWithImports.__qualname__ = "ComplexObjectWithImports"
with_imports_ComplexObjectWithImports.__module__ = "package_name.with_imports"


class with_imports_ImportService(Service):

    def test_endpoint(self, imported_string: "product_StringExample") -> "product_datasets_BackingFileSystem":

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, Any] = {
        }

        _json: Any = ConjureEncoder().default(imported_string)

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), product_datasets_BackingFileSystem, self._return_none_for_unknown_union_types)


with_imports_ImportService.__name__ = "ImportService"
with_imports_ImportService.__qualname__ = "ImportService"
with_imports_ImportService.__module__ = "package_name.with_imports"


class with_imports_ImportedAliasInMaps(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'aliases': ConjureFieldDefinition('aliases', Dict[product_RidAliasExample, product_DateTimeAliasExample])
        }

    __slots__: List[str] = ['_aliases']

    def __init__(self, aliases: Dict[str, str]) -> None:
        self._aliases = aliases

    @builtins.property
    def aliases(self) -> Dict[str, str]:
        return self._aliases


with_imports_ImportedAliasInMaps.__name__ = "ImportedAliasInMaps"
with_imports_ImportedAliasInMaps.__qualname__ = "ImportedAliasInMaps"
with_imports_ImportedAliasInMaps.__module__ = "package_name.with_imports"


class with_imports_UnionWithImports(ConjureUnionType):
    _string: Optional[str] = None
    _imported: Optional["product_AnyMapExample"] = None

    @builtins.classmethod
    def _options(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', product_AnyMapExample)
        }

    def __init__(
            self,
            string: Optional[str] = None,
            imported: Optional["product_AnyMapExample"] = None,
            type_of_union: Optional[str] = None
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
    def string(self) -> Optional[str]:
        return self._string

    @builtins.property
    def imported(self) -> Optional["product_AnyMapExample"]:
        return self._imported

    def accept(self, visitor) -> Any:
        if not isinstance(visitor, with_imports_UnionWithImportsVisitor):
            raise ValueError('{} is not an instance of with_imports_UnionWithImportsVisitor'.format(visitor.__class__.__name__))
        if self._type == 'string' and self.string is not None:
            return visitor._string(self.string)
        if self._type == 'imported' and self.imported is not None:
            return visitor._imported(self.imported)


with_imports_UnionWithImports.__name__ = "UnionWithImports"
with_imports_UnionWithImports.__qualname__ = "UnionWithImports"
with_imports_UnionWithImports.__module__ = "package_name.with_imports"


class with_imports_UnionWithImportsVisitor:

    @abstractmethod
    def _string(self, string: str) -> Any:
        pass

    @abstractmethod
    def _imported(self, imported: "product_AnyMapExample") -> Any:
        pass


with_imports_UnionWithImportsVisitor.__name__ = "UnionWithImportsVisitor"
with_imports_UnionWithImportsVisitor.__qualname__ = "UnionWithImportsVisitor"
with_imports_UnionWithImportsVisitor.__module__ = "package_name.with_imports"


product_IntegerAliasExample = int

with_imports_AliasImportedObject = product_ManyFieldExample

product_BooleanAliasExample = bool

product_RidAliasExample = str

product_BearerTokenAliasExample = str

product_BinaryAliasExample = BinaryType

product_DateTimeAliasExample = str

product_MapAliasExample = Dict[str, object]

package_name_TypeInPackageWithTheSameNameAsRootPackage = str

product_ReferenceAliasExample = product_AnyExample

product_UuidAliasExample = str

product_StringAliasExample = str

with_imports_AliasImportedPrimitiveAlias = product_StringAliasExample

with_imports_AliasImportedReferenceAlias = product_ReferenceAliasExample

product_DoubleAliasExample = float

product_RecursiveObjectAlias = product_RecursiveObjectExample

product_CollectionAliasExample = Dict[product_StringAliasExample, product_RecursiveObjectAlias]

product_NestedAliasExample = product_RecursiveObjectAlias

product_SafeLongAliasExample = int

