# coding=utf-8
import builtins
from conjure_python_client import (
    ConjureBeanType,
    ConjureFieldDefinition,
)
from typing import (
    Dict,
    List,
)

class test_api_FieldOrderTest(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'zebra': ConjureFieldDefinition('zebra', str),
            'apple': ConjureFieldDefinition('apple', str),
            'middle': ConjureFieldDefinition('middle', str),
            'banana': ConjureFieldDefinition('banana', str)
        }

    __slots__: List[str] = ['_zebra', '_apple', '_middle', '_banana']

    def __init__(self, apple: str, banana: str, middle: str, zebra: str) -> None:
        self._zebra = zebra
        self._apple = apple
        self._middle = middle
        self._banana = banana

    @builtins.property
    def zebra(self) -> str:
        return self._zebra

    @builtins.property
    def apple(self) -> str:
        return self._apple

    @builtins.property
    def middle(self) -> str:
        return self._middle

    @builtins.property
    def banana(self) -> str:
        return self._banana


test_api_FieldOrderTest.__name__ = "FieldOrderTest"
test_api_FieldOrderTest.__qualname__ = "FieldOrderTest"
test_api_FieldOrderTest.__module__ = "package_name.test_api"


