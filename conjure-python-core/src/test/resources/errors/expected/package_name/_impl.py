# coding=utf-8
import builtins
from conjure_python_client import (
    ConjureBeanType,
    ConjureDecoder,
    ConjureEncoder,
    ConjureFieldDefinition,
    ConjureHTTPError,
    Service,
)
from requests.adapters import (
    Response,
)
from typing import (
    Any,
    Dict,
    List,
    TypedDict,
)
from urllib.parse import (
    quote,
)

class product_CategoryNotFound(ConjureHTTPError):
    """Thrown when the requested recipe category doesn't exist
    """

    ERROR_CODE = "NOT_FOUND"
    ERROR_NAMESPACE = "Recipe"
    ERROR_NAME = "CategoryNotFound"

    class SafeArgs(TypedDict):
        category_id: str
        available_categories: List[str]

    def __init__(self, base_error: ConjureHTTPError) -> None:
        super().__init__(
            status_code=base_error.status_code,
            error_code=base_error.error_code,
            error_name=base_error.error_name,
            error_instance_id=base_error.error_instance_id,
            parameters=base_error.parameters
        )
        self.safe_args: product_CategoryNotFound.SafeArgs = {
            'category_id': base_error.parameters['categoryId'],
            'available_categories': base_error.parameters['availableCategories']
        }

    @classmethod
    def is_instance(cls, error: ConjureHTTPError) -> bool:
        """Check if a ConjureHTTPError is this specific error type"""
        return (
            error.error_name == cls.ERROR_NAME and
            error.error_code == cls.ERROR_CODE
        )

    @classmethod
    def from_error(cls, error: ConjureHTTPError) -> 'product_CategoryNotFound':
        """Convert a generic ConjureHTTPError to this typed error"""
        if not cls.is_instance(error):
            raise ValueError(f"Error is not a {cls.ERROR_NAME}")
        return cls(error)


product_CategoryNotFound.__name__ = "CategoryNotFound"
product_CategoryNotFound.__qualname__ = "CategoryNotFound"
product_CategoryNotFound.__module__ = "package_name.product"


class product_InvalidIngredient(ConjureHTTPError):
    """Thrown when an ingredient is invalid for the recipe
    """

    ERROR_CODE = "INVALID_ARGUMENT"
    ERROR_NAMESPACE = "Recipe"
    ERROR_NAME = "InvalidIngredient"

    class SafeArgs(TypedDict):
        ingredient_name: str

    class UnsafeArgs(TypedDict):
        user_id: str

    def __init__(self, base_error: ConjureHTTPError) -> None:
        super().__init__(
            status_code=base_error.status_code,
            error_code=base_error.error_code,
            error_name=base_error.error_name,
            error_instance_id=base_error.error_instance_id,
            parameters=base_error.parameters
        )
        self.safe_args: product_InvalidIngredient.SafeArgs = {
            'ingredient_name': base_error.parameters['ingredientName']
        }
        self.unsafe_args: product_InvalidIngredient.UnsafeArgs = {
            'user_id': base_error.parameters['userId']
        }

    @classmethod
    def is_instance(cls, error: ConjureHTTPError) -> bool:
        """Check if a ConjureHTTPError is this specific error type"""
        return (
            error.error_name == cls.ERROR_NAME and
            error.error_code == cls.ERROR_CODE
        )

    @classmethod
    def from_error(cls, error: ConjureHTTPError) -> 'product_InvalidIngredient':
        """Convert a generic ConjureHTTPError to this typed error"""
        if not cls.is_instance(error):
            raise ValueError(f"Error is not a {cls.ERROR_NAME}")
        return cls(error)


product_InvalidIngredient.__name__ = "InvalidIngredient"
product_InvalidIngredient.__qualname__ = "InvalidIngredient"
product_InvalidIngredient.__module__ = "package_name.product"


class product_Recipe(ConjureBeanType):

    @builtins.classmethod
    def _fields(cls) -> Dict[str, ConjureFieldDefinition]:
        return {
            'name': ConjureFieldDefinition('name', str),
            'ingredients': ConjureFieldDefinition('ingredients', List[str])
        }

    __slots__: List[str] = ['_name', '_ingredients']

    def __init__(self, ingredients: List[str], name: str) -> None:
        self._name = name
        self._ingredients = ingredients

    @builtins.property
    def name(self) -> str:
        return self._name

    @builtins.property
    def ingredients(self) -> List[str]:
        return self._ingredients


product_Recipe.__name__ = "Recipe"
product_Recipe.__qualname__ = "Recipe"
product_Recipe.__module__ = "package_name.product"


class product_RecipeService(Service):

    def get_recipes_by_category(self, category_id: str) -> List["product_Recipe"]:
        """Get recipes by category
        """
        _conjure_encoder = ConjureEncoder()

        _headers: Dict[str, Any] = {
            'Accept': 'application/json',
        }

        _params: Dict[str, Any] = {
        }

        _path_params: Dict[str, str] = {
            'categoryId': quote(str(_conjure_encoder.default(category_id)), safe=''),
        }

        _json: Any = None

        _path = '/recipes/category/{categoryId}'
        _path = _path.format(**_path_params)

        _response: Response = self._request(
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), List[product_Recipe], self._return_none_for_unknown_union_types)


product_RecipeService.__name__ = "RecipeService"
product_RecipeService.__qualname__ = "RecipeService"
product_RecipeService.__module__ = "package_name.product"


