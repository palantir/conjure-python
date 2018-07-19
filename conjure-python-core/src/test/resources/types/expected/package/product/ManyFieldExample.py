from . import StringAliasExample
from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition
from conjure_python_client import DictType
from conjure_python_client import ListType
from conjure_python_client import OptionalType
from typing import Dict
from typing import List
from typing import Optional
from typing import Set

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
            'alias': ConjureFieldDefinition('alias', StringAliasExample.StringAliasExample)
        }

    _string = None # type: str
    _integer = None # type: int
    _double_value = None # type: float
    _optional_item = None # type: Optional[str]
    _items = None # type: List[str]
    _set = None # type: List[str]
    _map = None # type: Dict[str, str]
    _alias = None # type: StringAliasExample.StringAliasExample

    def __init__(self, string, integer, double_value, optional_item, items, set, map, alias):
        # type: (str, int, float, Optional[str], List[str], List[str], Dict[str, str], StringAliasExample.StringAliasExample) -> None
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
        # type: () -> StringAliasExample.StringAliasExample
        """docs for alias field"""
        return self._alias

