from abc import ABCMeta, abstractmethod
import builtins
from conjure_python_client import BinaryType, ConjureBeanType, ConjureEnumType, ConjureFieldDefinition, ConjureUnionType, DictType, ListType, OptionalType
from typing import Any, Dict, List, Optional, Set

class AliasAsMapKeyExample(ConjureBeanType):

    @builtins.classmethod
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

    __slots__ = ['_strings', '_rids', '_bearertokens', '_integers', '_safelongs', '_datetimes', '_uuids'] # type: List[str]

    def __init__(self, bearertokens, datetimes, integers, rids, safelongs, strings, uuids):
        # type: (Dict[BearerTokenAliasExample, ManyFieldExample], Dict[DateTimeAliasExample, ManyFieldExample], Dict[IntegerAliasExample, ManyFieldExample], Dict[RidAliasExample, ManyFieldExample], Dict[SafeLongAliasExample, ManyFieldExample], Dict[StringAliasExample, ManyFieldExample], Dict[UuidAliasExample, ManyFieldExample]) -> None
        self._strings = strings
        self._rids = rids
        self._bearertokens = bearertokens
        self._integers = integers
        self._safelongs = safelongs
        self._datetimes = datetimes
        self._uuids = uuids

    @builtins.property
    def strings(self):
        # type: () -> Dict[StringAliasExample, ManyFieldExample]
        return self._strings

    @builtins.property
    def rids(self):
        # type: () -> Dict[RidAliasExample, ManyFieldExample]
        return self._rids

    @builtins.property
    def bearertokens(self):
        # type: () -> Dict[BearerTokenAliasExample, ManyFieldExample]
        return self._bearertokens

    @builtins.property
    def integers(self):
        # type: () -> Dict[IntegerAliasExample, ManyFieldExample]
        return self._integers

    @builtins.property
    def safelongs(self):
        # type: () -> Dict[SafeLongAliasExample, ManyFieldExample]
        return self._safelongs

    @builtins.property
    def datetimes(self):
        # type: () -> Dict[DateTimeAliasExample, ManyFieldExample]
        return self._datetimes

    @builtins.property
    def uuids(self):
        # type: () -> Dict[UuidAliasExample, ManyFieldExample]
        return self._uuids

class AnyExample(ConjureBeanType):

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

class AnyMapExample(ConjureBeanType):

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

class BearerTokenExample(ConjureBeanType):

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

class BinaryExample(ConjureBeanType):

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

class BooleanExample(ConjureBeanType):

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

class CreateDatasetRequest(ConjureBeanType):

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

class DateTimeExample(ConjureBeanType):

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

class DoubleExample(ConjureBeanType):

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

class EmptyObjectExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
        }

    __slots__ = [] # type: List[str]


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

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'enum': ConjureFieldDefinition('enum', EnumExample)
        }

    __slots__ = ['_enum'] # type: List[str]

    def __init__(self, enum):
        # type: (EnumExample) -> None
        self._enum = enum

    @builtins.property
    def enum(self):
        # type: () -> EnumExample
        return self._enum

class FieldObject(ConjureBeanType):

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

class IntegerExample(ConjureBeanType):

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

class ListExample(ConjureBeanType):

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

class ManyFieldExample(ConjureBeanType):

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
            'alias': ConjureFieldDefinition('alias', StringAliasExample)
        }

    __slots__ = ['_string', '_integer', '_double_value', '_optional_item', '_items', '_set', '_map', '_alias'] # type: List[str]

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

    @builtins.property
    def string(self):
        # type: () -> str
        """docs for string field"""
        return self._string

    @builtins.property
    def integer(self):
        # type: () -> int
        """docs for integer field"""
        return self._integer

    @builtins.property
    def double_value(self):
        # type: () -> float
        """docs for doubleValue field"""
        return self._double_value

    @builtins.property
    def optional_item(self):
        # type: () -> Optional[str]
        """docs for optionalItem field"""
        return self._optional_item

    @builtins.property
    def items(self):
        # type: () -> List[str]
        """docs for items field"""
        return self._items

    @builtins.property
    def set(self):
        # type: () -> List[str]
        """docs for set field"""
        return self._set

    @builtins.property
    def map(self):
        # type: () -> Dict[str, str]
        """docs for map field"""
        return self._map

    @builtins.property
    def alias(self):
        # type: () -> StringAliasExample
        """docs for alias field"""
        return self._alias

class MapExample(ConjureBeanType):

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

class OptionalExample(ConjureBeanType):

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

class PrimitiveOptionalsExample(ConjureBeanType):

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

class RecursiveObjectExample(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'recursive_field': ConjureFieldDefinition('recursiveField', OptionalType(RecursiveObjectAlias))
        }

    __slots__ = ['_recursive_field'] # type: List[str]

    def __init__(self, recursive_field=None):
        # type: (Optional[RecursiveObjectAlias]) -> None
        self._recursive_field = recursive_field

    @builtins.property
    def recursive_field(self):
        # type: () -> Optional[RecursiveObjectAlias]
        return self._recursive_field

class ReservedKeyExample(ConjureBeanType):

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

class RidExample(ConjureBeanType):

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

class SafeLongExample(ConjureBeanType):

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

class SetExample(ConjureBeanType):

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

class StringExample(ConjureBeanType):

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

class UnionTypeExample(ConjureUnionType):
    """A type which can either be a StringExample, a set of strings, or an integer."""

    _string_example = None # type: StringExample
    _set = None # type: List[str]
    _this_field_is_an_integer = None # type: int
    _also_an_integer = None # type: int
    _if = None # type: int
    _new = None # type: int
    _interface = None # type: int

    @builtins.classmethod
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
            self._if_ = if_
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
        return self._if_

    @property
    def new(self):
        # type: () -> int
        return self._new

    @property
    def interface(self):
        # type: () -> int
        return self._interface

    def accept(self, visitor):
        # type: (UnionTypeExampleVisitor) -> Any
        if not isinstance(visitor, UnionTypeExampleVisitor):
            raise ValueError('{} is not an instance of UnionTypeExampleVisitor'.format(visitor.__class__.__name__))
        if self.type == 'stringExample':
            return visitor._string_example(self.string_example)
        if self.type == 'set':
            return visitor._set(self.set)
        if self.type == 'thisFieldIsAnInteger':
            return visitor._this_field_is_an_integer(self.this_field_is_an_integer)
        if self.type == 'alsoAnInteger':
            return visitor._also_an_integer(self.also_an_integer)
        if self.type == 'if':
            return visitor._if(self.if_)
        if self.type == 'new':
            return visitor._new(self.new)
        if self.type == 'interface':
            return visitor._interface(self.interface)


UnionTypeExampleVisitorBaseClass = ABCMeta('ABC', (object,), {}) # type: Any


class UnionTypeExampleVisitor(UnionTypeExampleVisitorBaseClass):

    @abstractmethod
    def _string_example(self, string_example):
        # type: (StringExample) -> Any
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


class UuidExample(ConjureBeanType):

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

