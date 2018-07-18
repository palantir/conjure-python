from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition
from conjure import ListType
from typing import List

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

    def __init__(self, items, primitive_items, double_items):
        # type: (List[str], List[int], List[float]) -> None
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

