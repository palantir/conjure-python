from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition
from conjure_python_client import ListType
from typing import Set

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

    def __init__(self, items, double_items):
        # type: (List[str], List[float]) -> None
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

