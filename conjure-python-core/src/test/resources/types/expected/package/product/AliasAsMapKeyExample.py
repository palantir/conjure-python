from . import BearerTokenAliasExample
from . import DateTimeAliasExample
from . import IntegerAliasExample
from . import ManyFieldExample
from . import RidAliasExample
from . import SafeLongAliasExample
from . import StringAliasExample
from . import UuidAliasExample
from conjure_python_client import ConjureBeanType
from conjure_python_client import ConjureFieldDefinition
from conjure_python_client import DictType
from typing import Dict

class AliasAsMapKeyExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'strings': ConjureFieldDefinition('strings', DictType(StringAliasExample.StringAliasExample, ManyFieldExample.ManyFieldExample)),
            'rids': ConjureFieldDefinition('rids', DictType(RidAliasExample.RidAliasExample, ManyFieldExample.ManyFieldExample)),
            'bearertokens': ConjureFieldDefinition('bearertokens', DictType(BearerTokenAliasExample.BearerTokenAliasExample, ManyFieldExample.ManyFieldExample)),
            'integers': ConjureFieldDefinition('integers', DictType(IntegerAliasExample.IntegerAliasExample, ManyFieldExample.ManyFieldExample)),
            'safelongs': ConjureFieldDefinition('safelongs', DictType(SafeLongAliasExample.SafeLongAliasExample, ManyFieldExample.ManyFieldExample)),
            'datetimes': ConjureFieldDefinition('datetimes', DictType(DateTimeAliasExample.DateTimeAliasExample, ManyFieldExample.ManyFieldExample)),
            'uuids': ConjureFieldDefinition('uuids', DictType(UuidAliasExample.UuidAliasExample, ManyFieldExample.ManyFieldExample))
        }

    _strings = None # type: Dict[StringAliasExample.StringAliasExample, ManyFieldExample.ManyFieldExample]
    _rids = None # type: Dict[RidAliasExample.RidAliasExample, ManyFieldExample.ManyFieldExample]
    _bearertokens = None # type: Dict[BearerTokenAliasExample.BearerTokenAliasExample, ManyFieldExample.ManyFieldExample]
    _integers = None # type: Dict[IntegerAliasExample.IntegerAliasExample, ManyFieldExample.ManyFieldExample]
    _safelongs = None # type: Dict[SafeLongAliasExample.SafeLongAliasExample, ManyFieldExample.ManyFieldExample]
    _datetimes = None # type: Dict[DateTimeAliasExample.DateTimeAliasExample, ManyFieldExample.ManyFieldExample]
    _uuids = None # type: Dict[UuidAliasExample.UuidAliasExample, ManyFieldExample.ManyFieldExample]

    def __init__(self, strings, rids, bearertokens, integers, safelongs, datetimes, uuids):
        # type: (Dict[StringAliasExample.StringAliasExample, ManyFieldExample.ManyFieldExample], Dict[RidAliasExample.RidAliasExample, ManyFieldExample.ManyFieldExample], Dict[BearerTokenAliasExample.BearerTokenAliasExample, ManyFieldExample.ManyFieldExample], Dict[IntegerAliasExample.IntegerAliasExample, ManyFieldExample.ManyFieldExample], Dict[SafeLongAliasExample.SafeLongAliasExample, ManyFieldExample.ManyFieldExample], Dict[DateTimeAliasExample.DateTimeAliasExample, ManyFieldExample.ManyFieldExample], Dict[UuidAliasExample.UuidAliasExample, ManyFieldExample.ManyFieldExample]) -> None
        self._strings = strings
        self._rids = rids
        self._bearertokens = bearertokens
        self._integers = integers
        self._safelongs = safelongs
        self._datetimes = datetimes
        self._uuids = uuids

    @property
    def strings(self):
        # type: () -> Dict[StringAliasExample.StringAliasExample, ManyFieldExample.ManyFieldExample]
        return self._strings

    @property
    def rids(self):
        # type: () -> Dict[RidAliasExample.RidAliasExample, ManyFieldExample.ManyFieldExample]
        return self._rids

    @property
    def bearertokens(self):
        # type: () -> Dict[BearerTokenAliasExample.BearerTokenAliasExample, ManyFieldExample.ManyFieldExample]
        return self._bearertokens

    @property
    def integers(self):
        # type: () -> Dict[IntegerAliasExample.IntegerAliasExample, ManyFieldExample.ManyFieldExample]
        return self._integers

    @property
    def safelongs(self):
        # type: () -> Dict[SafeLongAliasExample.SafeLongAliasExample, ManyFieldExample.ManyFieldExample]
        return self._safelongs

    @property
    def datetimes(self):
        # type: () -> Dict[DateTimeAliasExample.DateTimeAliasExample, ManyFieldExample.ManyFieldExample]
        return self._datetimes

    @property
    def uuids(self):
        # type: () -> Dict[UuidAliasExample.UuidAliasExample, ManyFieldExample.ManyFieldExample]
        return self._uuids

