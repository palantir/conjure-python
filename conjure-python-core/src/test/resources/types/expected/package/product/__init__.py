from conjure_python_client import BinaryType, ConjureBeanType, ConjureEnumType, ConjureFieldDefinition, ConjureUnionType, DictType, ListType, OptionalType
from typing import Any, List, Optional, Set

class AliasAsMapKeyExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'strings': ConjureFieldDefinition('strings', DictType(StringAliasExample, ManyFieldExample)),
            'rids': ConjureFieldDefinition('rids', DictType(RidAliasExample, ManyFieldExample)),
            'bearertokens': ConjureFieldDefinition('bearertokens', DictType(BearerTokenAliasExample, ManyFieldExample)),
            'integers': ConjureFieldDefinition('integers', DictType(IntegerAliasExample, ManyFieldExample)),
            'safelongs': ConjureFieldDefinition('safelongs', DictType(SafeLongAliasExample, ManyFieldExample)),
            'datetimes': ConjureFieldDefinition('datetimes', DictType(DateTimeAliasExample, ManyFieldExample)),
            'uuids': ConjureFieldDefinition('uuids', DictType(UuidAliasExample, ManyFieldExample))
        }

    _strings = None # type: Dict[StringAliasExample, ManyFieldExample]
    _rids = None # type: Dict[RidAliasExample, ManyFieldExample]
    _bearertokens = None # type: Dict[BearerTokenAliasExample, ManyFieldExample]
    _integers = None # type: Dict[IntegerAliasExample, ManyFieldExample]
    _safelongs = None # type: Dict[SafeLongAliasExample, ManyFieldExample]
    _datetimes = None # type: Dict[DateTimeAliasExample, ManyFieldExample]
    _uuids = None # type: Dict[UuidAliasExample, ManyFieldExample]

    def __init__(self, bearertokens, datetimes, integers, rids, safelongs, strings, uuids):
        # type: (Dict[BearerTokenAliasExample, ManyFieldExample], Dict[DateTimeAliasExample, ManyFieldExample], Dict[IntegerAliasExample, ManyFieldExample], Dict[RidAliasExample, ManyFieldExample], Dict[SafeLongAliasExample, ManyFieldExample], Dict[StringAliasExample, ManyFieldExample], Dict[UuidAliasExample, ManyFieldExample]) -> None
        self._strings = strings
        self._rids = rids
        self._bearertokens = bearertokens
        self._integers = integers
        self._safelongs = safelongs
        self._datetimes = datetimes
        self._uuids = uuids

    @property
    def strings(self):
        # type: () -> Dict[StringAliasExample, ManyFieldExample]
        return self._strings

    @property
    def rids(self):
        # type: () -> Dict[RidAliasExample, ManyFieldExample]
        return self._rids

    @property
    def bearertokens(self):
        # type: () -> Dict[BearerTokenAliasExample, ManyFieldExample]
        return self._bearertokens

    @property
    def integers(self):
        # type: () -> Dict[IntegerAliasExample, ManyFieldExample]
        return self._integers

    @property
    def safelongs(self):
        # type: () -> Dict[SafeLongAliasExample, ManyFieldExample]
        return self._safelongs

    @property
    def datetimes(self):
        # type: () -> Dict[DateTimeAliasExample, ManyFieldExample]
        return self._datetimes

    @property
    def uuids(self):
        # type: () -> Dict[UuidAliasExample, ManyFieldExample]
        return self._uuids

class AnyExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'any': ConjureFieldDefinition('any', object)
        }

    _any = None # type: Any

    def __init__(self, any):
        # type: (Any) -> None
        self._any = any

    @property
    def any(self):
        # type: () -> Any
        return self._any

class AnyMapExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', DictType(str, object))
        }

    _items = None # type: Dict[str, Any]

    def __init__(self, items):
        # type: (Dict[str, Any]) -> None
        self._items = items

    @property
    def items(self):
        # type: () -> Dict[str, Any]
        return self._items

class BearerTokenExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'bearer_token_value': ConjureFieldDefinition('bearerTokenValue', str)
        }

    _bearer_token_value = None # type: str

    def __init__(self, bearer_token_value):
        # type: (str) -> None
        self._bearer_token_value = bearer_token_value

    @property
    def bearer_token_value(self):
        # type: () -> str
        return self._bearer_token_value

class BinaryExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'binary': ConjureFieldDefinition('binary', BinaryType())
        }

    _binary = None # type: Any

    def __init__(self, binary):
        # type: (Any) -> None
        self._binary = binary

    @property
    def binary(self):
        # type: () -> Any
        return self._binary

class BooleanExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'coin': ConjureFieldDefinition('coin', bool)
        }

    _coin = None # type: bool

    def __init__(self, coin):
        # type: (bool) -> None
        self._coin = coin

    @property
    def coin(self):
        # type: () -> bool
        return self._coin

class CreateDatasetRequest(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'path': ConjureFieldDefinition('path', str)
        }

    _file_system_id = None # type: str
    _path = None # type: str

    def __init__(self, file_system_id, path):
        # type: (str, str) -> None
        self._file_system_id = file_system_id
        self._path = path

    @property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @property
    def path(self):
        # type: () -> str
        return self._path

class DateTimeExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'datetime': ConjureFieldDefinition('datetime', str)
        }

    _datetime = None # type: str

    def __init__(self, datetime):
        # type: (str) -> None
        self._datetime = datetime

    @property
    def datetime(self):
        # type: () -> str
        return self._datetime

class DoubleExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'double_value': ConjureFieldDefinition('doubleValue', float)
        }

    _double_value = None # type: float

    def __init__(self, double_value):
        # type: (float) -> None
        self._double_value = double_value

    @property
    def double_value(self):
        # type: () -> float
        return self._double_value

class EmptyObjectExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
        }



class EnumExample(ConjureEnumType):
    """This enumerates the numbers 1:2."""

    ONE = 'ONE'
    '''ONE'''
    TWO = 'TWO'
    '''TWO'''
    UNKNOWN = 'UNKNOWN'
    '''UNKNOWN'''

    def __reduce_ex__(self, proto):
        return self.__class__, (self.name,)

class EnumFieldExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'enum': ConjureFieldDefinition('enum', EnumExample)
        }

    _enum = None # type: EnumExample

    def __init__(self, enum):
        # type: (EnumExample) -> None
        self._enum = enum

    @property
    def enum(self):
        # type: () -> EnumExample
        return self._enum

class IntegerExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'integer': ConjureFieldDefinition('integer', int)
        }

    _integer = None # type: int

    def __init__(self, integer):
        # type: (int) -> None
        self._integer = integer

    @property
    def integer(self):
        # type: () -> int
        return self._integer

class ListExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', ListType(str)),
            'primitive_items': ConjureFieldDefinition('primitiveItems', ListType(int)),
            'double_items': ConjureFieldDefinition('doubleItems', ListType(float))
        }

    _items = None # type: List[str]
    _primitive_items = None # type: List[int]
    _double_items = None # type: List[float]

    def __init__(self, double_items, items, primitive_items):
        # type: (List[float], List[str], List[int]) -> None
        self._items = items
        self._primitive_items = primitive_items
        self._double_items = double_items

    @property
    def items(self):
        # type: () -> List[str]
        return self._items

    @property
    def primitive_items(self):
        # type: () -> List[int]
        return self._primitive_items

    @property
    def double_items(self):
        # type: () -> List[float]
        return self._double_items

class ManyFieldExample(ConjureBeanType):

    @classmethod
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
            'alias': ConjureFieldDefinition('alias', StringAliasExample)
        }

    _string = None # type: str
    _integer = None # type: int
    _double_value = None # type: float
    _optional_item = None # type: Optional[str]
    _items = None # type: List[str]
    _set = None # type: List[str]
    _map = None # type: Dict[str, str]
    _alias = None # type: StringAliasExample

    def __init__(self, alias, double_value, integer, items, map, set, string, optional_item=None):
        # type: (StringAliasExample, float, int, List[str], Dict[str, str], List[str], str, Optional[str]) -> None
        self._string = string
        self._integer = integer
        self._double_value = double_value
        self._optional_item = optional_item
        self._items = items
        self._set = set
        self._map = map
        self._alias = alias

    @property
    def string(self):
        # type: () -> str
        """docs for string field"""
        return self._string

    @property
    def integer(self):
        # type: () -> int
        """docs for integer field"""
        return self._integer

    @property
    def double_value(self):
        # type: () -> float
        """docs for doubleValue field"""
        return self._double_value

    @property
    def optional_item(self):
        # type: () -> Optional[str]
        """docs for optionalItem field"""
        return self._optional_item

    @property
    def items(self):
        # type: () -> List[str]
        """docs for items field"""
        return self._items

    @property
    def set(self):
        # type: () -> List[str]
        """docs for set field"""
        return self._set

    @property
    def map(self):
        # type: () -> Dict[str, str]
        """docs for map field"""
        return self._map

    @property
    def alias(self):
        # type: () -> StringAliasExample
        """docs for alias field"""
        return self._alias

class MapExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', DictType(str, str))
        }

    _items = None # type: Dict[str, str]

    def __init__(self, items):
        # type: (Dict[str, str]) -> None
        self._items = items

    @property
    def items(self):
        # type: () -> Dict[str, str]
        return self._items

class OptionalExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'item': ConjureFieldDefinition('item', OptionalType(str))
        }

    _item = None # type: Optional[str]

    def __init__(self, item=None):
        # type: (Optional[str]) -> None
        self._item = item

    @property
    def item(self):
        # type: () -> Optional[str]
        return self._item

class PrimitiveOptionalsExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'num': ConjureFieldDefinition('num', OptionalType(float)),
            'bool': ConjureFieldDefinition('bool', OptionalType(bool)),
            'integer': ConjureFieldDefinition('integer', OptionalType(int)),
            'safelong': ConjureFieldDefinition('safelong', OptionalType(int)),
            'rid': ConjureFieldDefinition('rid', OptionalType(str)),
            'bearertoken': ConjureFieldDefinition('bearertoken', OptionalType(str)),
            'uuid': ConjureFieldDefinition('uuid', OptionalType(str))
        }

    _num = None # type: Optional[float]
    _bool = None # type: Optional[bool]
    _integer = None # type: Optional[int]
    _safelong = None # type: Optional[int]
    _rid = None # type: Optional[str]
    _bearertoken = None # type: Optional[str]
    _uuid = None # type: Optional[str]

    def __init__(self, bearertoken=None, bool=None, integer=None, num=None, rid=None, safelong=None, uuid=None):
        # type: (Optional[str], Optional[bool], Optional[int], Optional[float], Optional[str], Optional[int], Optional[str]) -> None
        self._num = num
        self._bool = bool
        self._integer = integer
        self._safelong = safelong
        self._rid = rid
        self._bearertoken = bearertoken
        self._uuid = uuid

    @property
    def num(self):
        # type: () -> Optional[float]
        return self._num

    @property
    def bool(self):
        # type: () -> Optional[bool]
        return self._bool

    @property
    def integer(self):
        # type: () -> Optional[int]
        return self._integer

    @property
    def safelong(self):
        # type: () -> Optional[int]
        return self._safelong

    @property
    def rid(self):
        # type: () -> Optional[str]
        return self._rid

    @property
    def bearertoken(self):
        # type: () -> Optional[str]
        return self._bearertoken

    @property
    def uuid(self):
        # type: () -> Optional[str]
        return self._uuid

class RecursiveObjectExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'recursive_field': ConjureFieldDefinition('recursiveField', OptionalType(RecursiveObjectAlias))
        }

    _recursive_field = None # type: Optional[RecursiveObjectAlias]

    def __init__(self, recursive_field=None):
        # type: (Optional[RecursiveObjectAlias]) -> None
        self._recursive_field = recursive_field

    @property
    def recursive_field(self):
        # type: () -> Optional[RecursiveObjectAlias]
        return self._recursive_field

class ReservedKeyExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'package': ConjureFieldDefinition('package', str),
            'interface': ConjureFieldDefinition('interface', str),
            'field_name_with_dashes': ConjureFieldDefinition('field-name-with-dashes', str),
            'memoized_hash_code': ConjureFieldDefinition('memoizedHashCode', int)
        }

    _package = None # type: str
    _interface = None # type: str
    _field_name_with_dashes = None # type: str
    _memoized_hash_code = None # type: int

    def __init__(self, field_name_with_dashes, interface, memoized_hash_code, package):
        # type: (str, str, int, str) -> None
        self._package = package
        self._interface = interface
        self._field_name_with_dashes = field_name_with_dashes
        self._memoized_hash_code = memoized_hash_code

    @property
    def package(self):
        # type: () -> str
        return self._package

    @property
    def interface(self):
        # type: () -> str
        return self._interface

    @property
    def field_name_with_dashes(self):
        # type: () -> str
        return self._field_name_with_dashes

    @property
    def memoized_hash_code(self):
        # type: () -> int
        return self._memoized_hash_code

class RidExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'rid_value': ConjureFieldDefinition('ridValue', str)
        }

    _rid_value = None # type: str

    def __init__(self, rid_value):
        # type: (str) -> None
        self._rid_value = rid_value

    @property
    def rid_value(self):
        # type: () -> str
        return self._rid_value

class SafeLongExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'safe_long_value': ConjureFieldDefinition('safeLongValue', int)
        }

    _safe_long_value = None # type: int

    def __init__(self, safe_long_value):
        # type: (int) -> None
        self._safe_long_value = safe_long_value

    @property
    def safe_long_value(self):
        # type: () -> int
        return self._safe_long_value

class SetExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'items': ConjureFieldDefinition('items', ListType(str)),
            'double_items': ConjureFieldDefinition('doubleItems', ListType(float))
        }

    _items = None # type: List[str]
    _double_items = None # type: List[float]

    def __init__(self, double_items, items):
        # type: (List[float], List[str]) -> None
        self._items = items
        self._double_items = double_items

    @property
    def items(self):
        # type: () -> List[str]
        return self._items

    @property
    def double_items(self):
        # type: () -> List[float]
        return self._double_items

class StringExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str)
        }

    _string = None # type: str

    def __init__(self, string):
        # type: (str) -> None
        self._string = string

    @property
    def string(self):
        # type: () -> str
        return self._string

class UnionTypeExample(ConjureUnionType):
    """A type which can either be a StringExample, a set of strings, or an integer."""

    _string_example = None # type: StringExample
    _set = None # type: List[str]
    _this_field_is_an_integer = None # type: int
    _also_an_integer = None # type: int
    _if = None # type: int
    _new = None # type: int
    _interface = None # type: int

    @classmethod
    def _options(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string_example': ConjureFieldDefinition('stringExample', StringExample),
            'set': ConjureFieldDefinition('set', ListType(str)),
            'this_field_is_an_integer': ConjureFieldDefinition('thisFieldIsAnInteger', int),
            'also_an_integer': ConjureFieldDefinition('alsoAnInteger', int),
            'if_': ConjureFieldDefinition('if', int),
            'new': ConjureFieldDefinition('new', int),
            'interface': ConjureFieldDefinition('interface', int)
        }

    def __init__(self, string_example=None, set=None, this_field_is_an_integer=None, also_an_integer=None, if_=None, new=None, interface=None):
        if (string_example is not None) + (set is not None) + (this_field_is_an_integer is not None) + (also_an_integer is not None) + (if_ is not None) + (new is not None) + (interface is not None) != 1:
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
            self._if = if_
            self._type = 'if'
        if new is not None:
            self._new = new
            self._type = 'new'
        if interface is not None:
            self._interface = interface
            self._type = 'interface'

    @property
    def string_example(self):
        # type: () -> StringExample
        """Docs for when UnionTypeExample is of type StringExample."""
        return self._string_example

    @property
    def set(self):
        # type: () -> List[str]
        return self._set

    @property
    def this_field_is_an_integer(self):
        # type: () -> int
        return self._this_field_is_an_integer

    @property
    def also_an_integer(self):
        # type: () -> int
        return self._also_an_integer

    @property
    def if_(self):
        # type: () -> int
        return self._if

    @property
    def new(self):
        # type: () -> int
        return self._new

    @property
    def interface(self):
        # type: () -> int
        return self._interface

class UuidExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'uuid': ConjureFieldDefinition('uuid', str)
        }

    _uuid = None # type: str

    def __init__(self, uuid):
        # type: (str) -> None
        self._uuid = uuid

    @property
    def uuid(self):
        # type: () -> str
        return self._uuid

BearerTokenAliasExample = str

BinaryAliasExample = BinaryType()

BooleanAliasExample = bool

DateTimeAliasExample = str

DoubleAliasExample = float

IntegerAliasExample = int

MapAliasExample = DictType(str, object)

RecursiveObjectAlias = RecursiveObjectExample

ReferenceAliasExample = AnyExample

RidAliasExample = str

SafeLongAliasExample = int

StringAliasExample = str

UuidAliasExample = str

