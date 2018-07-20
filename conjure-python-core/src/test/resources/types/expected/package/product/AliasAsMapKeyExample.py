from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition
from conjure_python_client import DictType
from typing import Dict

class AliasAsMapKeyExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'strings': ConjureFieldDefinition('strings', DictType(StringAliasExample, ManyFieldExample)),
            'rids': ConjureFieldDefinition('rids', DictType(RidAliasExample, ManyFieldExample)),
            'bearertokens': ConjureFieldDefinition('bearertokens', DictType(BearerTokenAliasExample, ManyFieldExample)),
            'integers': ConjureFieldDefinition('integers', DictType(IntegerAliasExample, ManyFieldExample)),
            'safelongs': ConjureFieldDefinition('safelongs', DictType(SafeLongAliasExample, ManyFieldExample)),
            'datetimes': ConjureFieldDefinition('datetimes', DictType(DateTimeAliasExample, ManyFieldExample)),
            'uuids': ConjureFieldDefinition('uuids', DictType(UuidAliasExample, ManyFieldExample))
        }

    _strings = None # type: Dict[StringAliasExample, ManyFieldExample]
    _rids = None # type: Dict[RidAliasExample, ManyFieldExample]
    _bearertokens = None # type: Dict[BearerTokenAliasExample, ManyFieldExample]
    _integers = None # type: Dict[IntegerAliasExample, ManyFieldExample]
    _safelongs = None # type: Dict[SafeLongAliasExample, ManyFieldExample]
    _datetimes = None # type: Dict[DateTimeAliasExample, ManyFieldExample]
    _uuids = None # type: Dict[UuidAliasExample, ManyFieldExample]

    def __init__(self, strings, rids, bearertokens, integers, safelongs, datetimes, uuids):
        # type: (Dict[StringAliasExample, ManyFieldExample], Dict[RidAliasExample, ManyFieldExample], Dict[BearerTokenAliasExample, ManyFieldExample], Dict[IntegerAliasExample, ManyFieldExample], Dict[SafeLongAliasExample, ManyFieldExample], Dict[DateTimeAliasExample, ManyFieldExample], Dict[UuidAliasExample, ManyFieldExample]) -> None
        self._strings = strings
        self._rids = rids
        self._bearertokens = bearertokens
        self._integers = integers
        self._safelongs = safelongs
        self._datetimes = datetimes
        self._uuids = uuids

    @property
    def strings(self):
        # type: () -> Dict[StringAliasExample, ManyFieldExample]
        return self._strings

    @property
    def rids(self):
        # type: () -> Dict[RidAliasExample, ManyFieldExample]
        return self._rids

    @property
    def bearertokens(self):
        # type: () -> Dict[BearerTokenAliasExample, ManyFieldExample]
        return self._bearertokens

    @property
    def integers(self):
        # type: () -> Dict[IntegerAliasExample, ManyFieldExample]
        return self._integers

    @property
    def safelongs(self):
        # type: () -> Dict[SafeLongAliasExample, ManyFieldExample]
        return self._safelongs

    @property
    def datetimes(self):
        # type: () -> Dict[DateTimeAliasExample, ManyFieldExample]
        return self._datetimes

    @property
    def uuids(self):
        # type: () -> Dict[UuidAliasExample, ManyFieldExample]
        return self._uuids

from .RidAliasExample import RidAliasExample
from .ManyFieldExample import ManyFieldExample
from .IntegerAliasExample import IntegerAliasExample
from .BearerTokenAliasExample import BearerTokenAliasExample
from .DateTimeAliasExample import DateTimeAliasExample
from .UuidAliasExample import UuidAliasExample
from .SafeLongAliasExample import SafeLongAliasExample
from .StringAliasExample import StringAliasExample
