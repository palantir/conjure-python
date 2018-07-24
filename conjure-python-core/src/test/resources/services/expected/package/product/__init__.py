# this is package package.product
from conjure_python_client import *
from typing import Dict
from typing import List
from typing import Optional
from typing import Set
from typing import Tuple

class CreateDatasetRequest(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'file_system_id': ConjureFieldDefinition('fileSystemId', str),
            'path': ConjureFieldDefinition('path', str),
            'favorite_color': ConjureFieldDefinition('favoriteColor', OptionalType(str))
        }

    _file_system_id = None # type: str
    _path = None # type: str
    _favorite_color = None # type: Optional[str]

    def __init__(self, file_system_id, path, favorite_color=None):
        # type: (str, str, Optional[str]) -> None
        self._file_system_id = file_system_id
        self._path = path
        self._favorite_color = favorite_color

    @property
    def file_system_id(self):
        # type: () -> str
        return self._file_system_id

    @property
    def path(self):
        # type: () -> str
        return self._path

    @property
    def favorite_color(self):
        # type: () -> Optional[str]
        return self._favorite_color

