from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

class RidExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'rid_value': ConjureFieldDefinition('ridValue', str)
        }

    _rid_value = None # type: str

    def __init__(self, rid_value):
        # type: (str) -> None
        self._rid_value = rid_value

    @property
    def rid_value(self):
        # type: () -> str
        return self._rid_value

