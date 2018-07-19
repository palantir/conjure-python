from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

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

