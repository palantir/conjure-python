from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

class ReservedKeyExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'package': ConjureFieldDefinition('package', str),
            'interface': ConjureFieldDefinition('interface', str),
            'field_name_with_dashes': ConjureFieldDefinition('field-name-with-dashes', str),
            'memoized_hash_code': ConjureFieldDefinition('memoizedHashCode', int)
        }

    _package = None # type: str
    _interface = None # type: str
    _field_name_with_dashes = None # type: str
    _memoized_hash_code = None # type: int

    def __init__(self, package, interface, field_name_with_dashes, memoized_hash_code):
        # type: (str, str, str, int) -> None
        self._package = package
        self._interface = interface
        self._field_name_with_dashes = field_name_with_dashes
        self._memoized_hash_code = memoized_hash_code

    @property
    def package(self):
        # type: () -> str
        return self._package

    @property
    def interface(self):
        # type: () -> str
        return self._interface

    @property
    def field_name_with_dashes(self):
        # type: () -> str
        return self._field_name_with_dashes

    @property
    def memoized_hash_code(self):
        # type: () -> int
        return self._memoized_hash_code

