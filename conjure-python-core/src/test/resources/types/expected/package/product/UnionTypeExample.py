from conjure_python_client import ConjureFieldDefinition
from conjure_python_client import ConjureUnionType
from conjure_python_client import ListType
from typing import Set

class UnionTypeExample(ConjureUnionType):
    """A type which can either be a StringExample, a set of strings, or an integer."""

    _string_example = None # type: StringExample
    _set = None # type: List[str]
    _this_field_is_an_integer = None # type: int
    _also_an_integer = None # type: int
    _if = None # type: int
    _new = None # type: int
    _interface = None # type: int

    @classmethod
    def _options(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'string_example': ConjureFieldDefinition('stringExample', StringExample),
            'set': ConjureFieldDefinition('set', ListType(str)),
            'this_field_is_an_integer': ConjureFieldDefinition('thisFieldIsAnInteger', int),
            'also_an_integer': ConjureFieldDefinition('alsoAnInteger', int),
            'if_': ConjureFieldDefinition('if', int),
            'new': ConjureFieldDefinition('new', int),
            'interface': ConjureFieldDefinition('interface', int)
        }

    def __init__(self, string_example=None, set=None, this_field_is_an_integer=None, also_an_integer=None, if_=None, new=None, interface=None):
        if (string_example is not None) + (set is not None) + (this_field_is_an_integer is not None) + (also_an_integer is not None) + (if_ is not None) + (new is not None) + (interface is not None) != 1:
            raise ValueError('a union must contain a single member')

        if string_example is not None:
            self._string_example = string_example
            self._type = 'stringExample'
        if set is not None:
            self._set = set
            self._type = 'set'
        if this_field_is_an_integer is not None:
            self._this_field_is_an_integer = this_field_is_an_integer
            self._type = 'thisFieldIsAnInteger'
        if also_an_integer is not None:
            self._also_an_integer = also_an_integer
            self._type = 'alsoAnInteger'
        if if_ is not None:
            self._if = if_
            self._type = 'if'
        if new is not None:
            self._new = new
            self._type = 'new'
        if interface is not None:
            self._interface = interface
            self._type = 'interface'

    @property
    def string_example(self):
        # type: () -> StringExample
        """Docs for when UnionTypeExample is of type StringExample."""
        return self._string_example

    @property
    def set(self):
        # type: () -> List[str]
        return self._set

    @property
    def this_field_is_an_integer(self):
        # type: () -> int
        return self._this_field_is_an_integer

    @property
    def also_an_integer(self):
        # type: () -> int
        return self._also_an_integer

    @property
    def if_(self):
        # type: () -> int
        return self._if

    @property
    def new(self):
        # type: () -> int
        return self._new

    @property
    def interface(self):
        # type: () -> int
        return self._interface

from .StringExample import StringExample
