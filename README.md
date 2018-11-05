# Conjure-Python ![Bintray](https://img.shields.io/bintray/v/palantir/releases/conjure-python.svg) [![License](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](https://opensource.org/licenses/Apache-2.0)

_CLI to generate Python classes from [Conjure API definitions](https://github.com/palantir/conjure)._

## Overview

The generated clients provide a simple [requests](http://docs.python-requests.org/en/master/) based interface for 
executing statically typed remote procedure calls from the Python 2 or 3.

## Usage

The recommended way to use conjure-python is via a build tool like [gradle-conjure](https://github.com/palantir/gradle-conjure). However, if you don't want to use gradle-conjure, there is also an executable which conforms to [RFC 002](https://github.com/palantir/conjure/blob/develop/rfc/002-contract-for-conjure-generators.md).

    Usage: conjure-python generate <input> <output> [...options]

        --packageName         package name that will appear in setup.py
        --packageVersion      version number that will appear in setup.py
        --packageDescription  description that will appear in setup.py
        --packageUrl          url that will appear in setup.py
        --packageAuthor       author that will appear in setup.py
        --writeCondaRecipe    use this boolean option to generate a `conda_recipe/meta.yaml`

## Example generated objects

- **Conjure object: [ManyFieldExample](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/types/expected/package/product/__init__.py#L345)**

    ```python
    example = ManyFieldExample('alias', 1.0, 1, [], {}, [])
    ```

- **Conjure union: [UnionTypeExample](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/types/expected/package/product/__init__.py#L689)**

    Union types can be one of a few variants. 

    ```python
    stringVariant = UnionTypeExample(string_example="foo")
    ```

- **Conjure enum: [EnumExample](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/types/expected/package/product/__init__.py#L256)**

  ```java
  one = EnumExample.ONE;
  print(one); // prints: 'ONE'
  ```

- **Conjure alias: [StringAliasExample](./conjure-java-core/src/integrationInput/java/com/palantir/product/StringAliasExample.java)**

  Python uses structural (duck-typing) so aliases are currently elided.

## Generated services

- [TestService](./conjure-python-core/src/test/resources/services/expected/package/another/TestService.py)

```python
from conjure_python_client import RequestsClient, ServiceConfiguration

config = ServiceConfiguration()
config.uris = ["https://foo.com/api"]
service = RequestsClient.create(TestService,
    user_agent="something/1.2.3",
    service_config=config)

service.do_something(auth_header, param)
```

## mypy typings

Generated code has [mypy](http://mypy-lang.org/) comments for optional static typing.

```diff
 class TestService(Service):
     '''A Markdown description of the service.'''

     def get_file_systems(self, authHeader):
+        # type: (str) -> Dict[str, BackingFileSystem]
```

## Contributing

See the [CONTRIBUTING.md](./CONTRIBUTING.md) document.
