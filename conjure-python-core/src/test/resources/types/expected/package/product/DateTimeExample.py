from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition

class DateTimeExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'datetime': ConjureFieldDefinition('datetime', str)
        }

    _datetime = None # type: str

    def __init__(self, datetime):
        # type: (str) -> None
        self._datetime = datetime

    @property
    def datetime(self):
        # type: () -> str
        return self._datetime

