from ..product.StringExample import StringExample
from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

class ComplexObjectWithImports(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', StringExample)
        }

    _string = None # type: str
    _imported = None # type: StringExample

    def __init__(self, string, imported):
        # type: (str, StringExample) -> None
        self._string = string
        self._imported = imported

    @property
    def string(self):
        # type: () -> str
        return self._string

    @property
    def imported(self):
        # type: () -> StringExample
        return self._imported

