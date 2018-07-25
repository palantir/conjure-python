# this is package package
from . import another
from . import nested_deeply_nested_service
from . import nested_service
from . import nested_service2
from . import product
from . import product_datasets
from . import with_imports

__all__ = [
    'another',
    'nested_deeply_nested_service',
    'nested_service',
    'nested_service2',
    'product',
    'product_datasets',
    'with_imports',
]

__version__ = "0.0.0"

__conjure_generator_version__ = "0.0.0"

