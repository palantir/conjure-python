from ..product import DateTimeAliasExample
from ..product import RidAliasExample
from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition
from conjure_python_client import DictType
from typing import Dict

class ImportedAliasInMaps(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'aliases': ConjureFieldDefinition('aliases', DictType(RidAliasExample.RidAliasExample, DateTimeAliasExample.DateTimeAliasExample))
        }

    _aliases = None # type: Dict[RidAliasExample, DateTimeAliasExample]

    def __init__(self, aliases):
        # type: (Dict[RidAliasExample, DateTimeAliasExample]) -> None
        self._aliases = aliases

    @property
    def aliases(self):
        # type: () -> Dict[RidAliasExample, DateTimeAliasExample]
        return self._aliases

