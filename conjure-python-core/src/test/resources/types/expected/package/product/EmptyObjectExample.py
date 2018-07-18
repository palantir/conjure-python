from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition

class EmptyObjectExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
        }



