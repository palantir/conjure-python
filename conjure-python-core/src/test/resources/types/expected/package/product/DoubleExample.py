from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition

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

