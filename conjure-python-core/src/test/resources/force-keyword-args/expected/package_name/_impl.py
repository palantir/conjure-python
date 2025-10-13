# coding=utf-8
from abc import (
    abstractmethod,
)
import builtins
from conjure_python_client import (
    ConjureBeanType,
    ConjureDecoder,
    ConjureEncoder,
    ConjureFieldDefinition,
    ConjureUnionType,
    Service,
)
from requests.adapters import (
    Response,
)
from typing import (
    Any,
    Dict,
    List,
    Optional,
)
from urllib.parse import (
    quote,
)

class test_api_KeywordArgsTest(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'name': ConjureFieldDefinition('name', str),
            'age': ConjureFieldDefinition('age', int),
            'email': ConjureFieldDefinition('email', str)
        }

    __slots__: List[str] = ['_name', '_age', '_email']

    def __init__(self, age: int, email: str, name: str) -> None:
        self._name = name
        self._age = age
        self._email = email

    @builtins.property
    def name(self) -> str:
        return self._name

    @builtins.property
    def age(self) -> int:
        return self._age

    @builtins.property
    def email(self) -> str:
        return self._email


test_api_KeywordArgsTest.__name__ = "KeywordArgsTest"
test_api_KeywordArgsTest.__qualname__ = "KeywordArgsTest"
test_api_KeywordArgsTest.__module__ = "package_name.test_api"


class test_api_TestService(Service):

    def test_endpoint(self, param1: str, param2: int) -> str:
        _conjure_encoder = ConjureEncoder()

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _params: Dict[str, Any] = {
            'param1': _conjure_encoder.default(param1),
            'param2': _conjure_encoder.default(param2),
        }

        _path_params: Dict[str, str] = {
        }

        _json: Any = None

        _path = '/test/test'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'POST',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), str, self._return_none_for_unknown_union_types)


test_api_TestService.__name__ = "TestService"
test_api_TestService.__qualname__ = "TestService"
test_api_TestService.__module__ = "package_name.test_api"


class test_api_UnionTest(ConjureUnionType):
    _foo: Optional[str] = None
    _bar: Optional[int] = None

    @builtins.classmethod
    def _options(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'foo': ConjureFieldDefinition('foo', str),
            'bar': ConjureFieldDefinition('bar', int)
        }

    def __init__(
            self,
            foo: Optional[str] = None,
            bar: Optional[int] = None,
            type_of_union: Optional[str] = None
            ) -> None:
        if type_of_union is None:
            if (foo is not None) + (bar is not None) != 1:
                raise ValueError('a union must contain a single member')

            if foo is not None:
                self._foo = foo
                self._type = 'foo'
            if bar is not None:
                self._bar = bar
                self._type = 'bar'

        elif type_of_union == 'foo':
            if foo is None:
                raise ValueError('a union value must not be None')
            self._foo = foo
            self._type = 'foo'
        elif type_of_union == 'bar':
            if bar is None:
                raise ValueError('a union value must not be None')
            self._bar = bar
            self._type = 'bar'

    @builtins.property
    def foo(self) -> Optional[str]:
        return self._foo

    @builtins.property
    def bar(self) -> Optional[int]:
        return self._bar

    def accept(self, visitor) -> Any:
        if not isinstance(visitor, test_api_UnionTestVisitor):
            raise ValueError('{} is not an instance of test_api_UnionTestVisitor'.format(visitor.__class__.__name__))
        if self._type == 'foo' and self.foo is not None:
            return visitor._foo(self.foo)
        if self._type == 'bar' and self.bar is not None:
            return visitor._bar(self.bar)


test_api_UnionTest.__name__ = "UnionTest"
test_api_UnionTest.__qualname__ = "UnionTest"
test_api_UnionTest.__module__ = "package_name.test_api"


class test_api_UnionTestVisitor:

    @abstractmethod
    def _foo(self, foo: str) -> Any:
        pass

    @abstractmethod
    def _bar(self, bar: int) -> Any:
        pass


test_api_UnionTestVisitor.__name__ = "UnionTestVisitor"
test_api_UnionTestVisitor.__qualname__ = "UnionTestVisitor"
test_api_UnionTestVisitor.__module__ = "package_name.test_api"


