from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition
from conjure import DictType
from typing import Dict

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

