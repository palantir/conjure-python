from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition
from conjure import OptionalType
from typing import Optional

class OptionalExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'item': ConjureFieldDefinition('item', OptionalType(str))
        }

    _item = None # type: Optional[str]

    def __init__(self, item):
        # type: (Optional[str]) -> None
        self._item = item

    @property
    def item(self):
        # type: () -> Optional[str]
        return self._item

