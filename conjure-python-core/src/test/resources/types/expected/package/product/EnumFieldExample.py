from .EnumExample import EnumExample
from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition

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

