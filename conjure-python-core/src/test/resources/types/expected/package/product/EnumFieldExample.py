from .EnumExample import EnumExample
from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

class EnumFieldExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'enum': ConjureFieldDefinition('enum', EnumExample)
        }

    _enum = None # type: EnumExample

    def __init__(self, enum):
        # type: (EnumExample) -> None
        self._enum = enum

    @property
    def enum(self):
        # type: () -> EnumExample
        return self._enum

