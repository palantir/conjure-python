from ..product.DateTimeAliasExample import DateTimeAliasExample
from ..product.RidAliasExample import RidAliasExample
from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition
from conjure import DictType
from typing import Dict

class ImportedAliasInMaps(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'aliases': ConjureFieldDefinition('aliases', DictType(RidAliasExample, DateTimeAliasExample))
        }

    _aliases = None # type: Dict[RidAliasExample, DateTimeAliasExample]

    def __init__(self, aliases):
        # type: (Dict[RidAliasExample, DateTimeAliasExample]) -> None
        self._aliases = aliases

    @property
    def aliases(self):
        # type: () -> Dict[RidAliasExample, DateTimeAliasExample]
        return self._aliases

