from conjure_python_client import ConjureBeanType, ConjureFieldDefinition, DictType

class BackingFileSystem(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'base_uri': ConjureFieldDefinition('baseUri', str),
            'configuration': ConjureFieldDefinition('configuration', DictType(str, str))
        }

    _file_system_id = None # type: str
    _base_uri = None # type: str
    _configuration = None # type: Dict[str, str]

    __slots__ = ['file_system_id', 'base_uri', 'configuration']

    def __init__(self, base_uri, configuration, file_system_id):
        # type: (str, Dict[str, str], str) -> None
        self._file_system_id = file_system_id
        self._base_uri = base_uri
        self._configuration = configuration

    @property
    def file_system_id(self):
        # type: () -> str
        """The name by which this file system is identified."""
        return self._file_system_id

    @property
    def base_uri(self):
        # type: () -> str
        return self._base_uri

    @property
    def configuration(self):
        # type: () -> Dict[str, str]
        return self._configuration

class Dataset(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'rid': ConjureFieldDefinition('rid', str)
        }

    _file_system_id = None # type: str
    _rid = None # type: str

    __slots__ = ['file_system_id', 'rid']

    def __init__(self, file_system_id, rid):
        # type: (str, str) -> None
        self._file_system_id = file_system_id
        self._rid = rid

    @property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @property
    def rid(self):
        # type: () -> str
        """Uniquely identifies this dataset."""
        return self._rid

