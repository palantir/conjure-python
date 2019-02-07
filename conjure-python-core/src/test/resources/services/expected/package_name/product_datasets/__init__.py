import builtins
from conjure_python_client import ConjureBeanType, ConjureFieldDefinition, DictType
from typing import Dict, List

class BackingFileSystem(ConjureBeanType):

    @builtins.classmethod
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

    def __init__(self, base_uri, configuration, file_system_id):
        # type: (str, Dict[str, str], str) -> None
        self._file_system_id = file_system_id
        self._base_uri = base_uri
        self._configuration = configuration

    @builtins.property
    def file_system_id(self):
        # type: () -> str
        """The name by which this file system is identified."""
        return self._file_system_id

    @builtins.property
    def base_uri(self):
        # type: () -> str
        return self._base_uri

    @builtins.property
    def configuration(self):
        # type: () -> Dict[str, str]
        return self._configuration

class Dataset(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'rid': ConjureFieldDefinition('rid', str)
        }

    _file_system_id = None # type: str
    _rid = None # type: str

    def __init__(self, file_system_id, rid):
        # type: (str, str) -> None
        self._file_system_id = file_system_id
        self._rid = rid

    @builtins.property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @builtins.property
    def rid(self):
        # type: () -> str
        """Uniquely identifies this dataset."""
        return self._rid

