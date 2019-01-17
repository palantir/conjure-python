import __builtin__
from conjure_python_client import ConjureBeanType, ConjureFieldDefinition

class CreateDatasetRequest(ConjureBeanType):

    @__builtin__.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'path': ConjureFieldDefinition('path', str)
        }

    _file_system_id = None # type: str
    _path = None # type: str

    def __init__(self, file_system_id, path):
        # type: (str, str) -> None
        self._file_system_id = file_system_id
        self._path = path

    @__builtin__.property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @__builtin__.property
    def path(self):
        # type: () -> str
        return self._path

