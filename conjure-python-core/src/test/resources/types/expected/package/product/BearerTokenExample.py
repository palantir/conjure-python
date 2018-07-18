from conjure import ConjureBeanType
from conjure import ConjureFieldDefinition

class BearerTokenExample(ConjureBeanType):

    @classmethod
    def _fields(cls):
        # type: () -> Dict[str, ConjureFieldDefinition]
        return {
            'bearer_token_value': ConjureFieldDefinition('bearerTokenValue', str)
        }

    _bearer_token_value = None # type: str

    def __init__(self, bearer_token_value):
        # type: (str) -> None
        self._bearer_token_value = bearer_token_value

    @property
    def bearer_token_value(self):
        # type: () -> str
        return self._bearer_token_value

