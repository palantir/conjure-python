from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

class UuidExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'uuid': ConjureFieldDefinition('uuid', str)
        }

    _uuid = None # type: str

    def __init__(self, uuid):
        # type: (str) -> None
        self._uuid = uuid

    @property
    def uuid(self):
        # type: () -> str
        return self._uuid

