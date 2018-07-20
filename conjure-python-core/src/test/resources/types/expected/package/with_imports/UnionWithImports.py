from ..product import AnyMapExample
from conjure_python_client import ConjureFieldDefinition
from conjure_python_client import ConjureUnionType

class UnionWithImports(ConjureUnionType):

    _string = None # type: str
    _imported = None # type: AnyMapExample

    @classmethod
    def _options(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', AnyMapExample.AnyMapExample)
        }

    def __init__(self, string=None, imported=None):
        if (string is not None) + (imported is not None) != 1:
            raise ValueError('a union must contain a single member')

        if string is not None:
            self._string = string
            self._type = 'string'
        if imported is not None:
            self._imported = imported
            self._type = 'imported'

    @property
    def string(self):
        # type: () -> str
        return self._string

    @property
    def imported(self):
        # type: () -> AnyMapExample
        return self._imported

