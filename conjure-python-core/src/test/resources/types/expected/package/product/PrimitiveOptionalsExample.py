from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition
from conjure import OptionalType
from typing import Optional

class PrimitiveOptionalsExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'num': ConjureFieldDefinition('num', OptionalType(float)),
            'bool': ConjureFieldDefinition('bool', OptionalType(bool)),
            'integer': ConjureFieldDefinition('integer', OptionalType(int)),
            'safelong': ConjureFieldDefinition('safelong', OptionalType(int)),
            'rid': ConjureFieldDefinition('rid', OptionalType(str)),
            'bearertoken': ConjureFieldDefinition('bearertoken', OptionalType(str)),
            'uuid': ConjureFieldDefinition('uuid', OptionalType(str))
        }

    _num = None # type: Optional[float]
    _bool = None # type: Optional[bool]
    _integer = None # type: Optional[int]
    _safelong = None # type: Optional[int]
    _rid = None # type: Optional[str]
    _bearertoken = None # type: Optional[str]
    _uuid = None # type: Optional[str]

    def __init__(self, num, bool, integer, safelong, rid, bearertoken, uuid):
        # type: (Optional[float], Optional[bool], Optional[int], Optional[int], Optional[str], Optional[str], Optional[str]) -> None
        self._num = num
        self._bool = bool
        self._integer = integer
        self._safelong = safelong
        self._rid = rid
        self._bearertoken = bearertoken
        self._uuid = uuid

    @property
    def num(self):
        # type: () -> Optional[float]
        return self._num

    @property
    def bool(self):
        # type: () -> Optional[bool]
        return self._bool

    @property
    def integer(self):
        # type: () -> Optional[int]
        return self._integer

    @property
    def safelong(self):
        # type: () -> Optional[int]
        return self._safelong

    @property
    def rid(self):
        # type: () -> Optional[str]
        return self._rid

    @property
    def bearertoken(self):
        # type: () -> Optional[str]
        return self._bearertoken

    @property
    def uuid(self):
        # type: () -> Optional[str]
        return self._uuid

