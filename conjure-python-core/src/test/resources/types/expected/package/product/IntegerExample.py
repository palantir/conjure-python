from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

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

