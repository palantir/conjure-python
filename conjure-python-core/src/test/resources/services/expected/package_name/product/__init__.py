import builtins
from conjure_python_client import ConjureBeanType, ConjureFieldDefinition
from typing import Dict

class CreateDatasetRequest(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'path': ConjureFieldDefinition('path', str)
        }

    __slots__ = ['_file_system_id', '_path'] # type: List[str]

    def __init__(self, file_system_id, path):
        # type: (str, str) -> None
        self._file_system_id = file_system_id
        self._path = path

    @builtins.property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @builtins.property
    def path(self):
        # type: () -> str
        return self._path

