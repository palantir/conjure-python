from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition

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

