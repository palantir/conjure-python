from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

class EmptyObjectExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
        }



