from ..product import AnyMapExample, DateTimeAliasExample, ManyFieldExample, ReferenceAliasExample, RidAliasExample, StringAliasExample, StringExample
from ..product_datasets import BackingFileSystem
from abc import ABCMeta, abstractmethod
from conjure_python_client import ConjureBeanType, ConjureDecoder, ConjureEncoder, ConjureFieldDefinition, ConjureUnionType, DictType, Service

class ComplexObjectWithImports(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', StringExample)
        }

    __slots__ = ['_string', '_imported']

    def __init__(self, imported, string):
        # type: (StringExample, str) -> None
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

class ImportService(Service):

    def test_endpoint(self, imported_string):
        # type: (StringExample) -> BackingFileSystem

        _headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = ConjureEncoder().default(imported_string) # type: Any

        _path = '/catalog/testEndpoint'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), BackingFileSystem)

class ImportedAliasInMaps(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'aliases': ConjureFieldDefinition('aliases', DictType(RidAliasExample, DateTimeAliasExample))
        }

    __slots__ = ['_aliases']

    def __init__(self, aliases):
        # type: (Dict[RidAliasExample, DateTimeAliasExample]) -> None
        self._aliases = aliases

    @property
    def aliases(self):
        # type: () -> Dict[RidAliasExample, DateTimeAliasExample]
        return self._aliases

class UnionWithImports(ConjureUnionType):

    _string = None # type: str
    _imported = None # type: AnyMapExample

    @classmethod
    def _options(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string': ConjureFieldDefinition('string', str),
            'imported': ConjureFieldDefinition('imported', AnyMapExample)
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

    def accept(self, visitor):
        # type: (UnionWithImportsVisitor) -> Any
        if not isinstance(visitor, UnionWithImportsVisitor):
            raise ValueError('{} is not an instance of UnionWithImportsVisitor'.format(visitor.__class__.__name__))
        if self.type == 'string':
            return visitor._string(self.string)
        if self.type == 'imported':
            return visitor._imported(self.imported)


class UnionWithImportsVisitor(ABCMeta('ABC', (object,), {})):

    @abstractmethod
    def _string(self, string):
        # type: (str) -> Any
        pass

    @abstractmethod
    def _imported(self, imported):
        # type: (AnyMapExample) -> Any
        pass


AliasImportedObject = ManyFieldExample

AliasImportedPrimitiveAlias = StringAliasExample

AliasImportedReferenceAlias = ReferenceAliasExample

