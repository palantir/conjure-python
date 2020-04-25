<p align="right">
<a href="https://autorelease.general.dmz.palantir.tech/palantir/conjure-python"><img src="https://img.shields.io/badge/Perform%20an-Autorelease-success.svg" alt="Autorelease"></a>
</p>

# Conjure-Python ![Bintray](https://img.shields.io/bintray/v/palantir/releases/conjure-python.svg) [![License](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](https://opensource.org/licenses/Apache-2.0)

_CLI to generate Python classes from [Conjure API definitions](https://github.com/palantir/conjure)._

## Overview

The generated clients provide a simple interface for executing statically typed remote procedure calls from Python 2 or 3.

## Usage

The recommended way to use conjure-python is via a build tool like [gradle-conjure](https://github.com/palantir/gradle-conjure). However, if you don't want to use gradle-conjure, there is also an executable which conforms to [RFC 002](https://github.com/palantir/conjure/blob/master/docs/rfc/002-contract-for-conjure-generators.md).

    Usage: conjure-python generate <input> <output> [...options]

        --packageName         package name that will appear in setup.py
        --packageVersion      version number that will appear in setup.py
        --packageDescription  description that will appear in setup.py
        --packageUrl          url that will appear in setup.py
        --packageAuthor       author that will appear in setup.py
        --writeCondaRecipe    use this boolean option to generate a `conda_recipe/meta.yaml`

## Example generated objects

- **Conjure object: [ManyFieldExample](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/types/expected/package_name/_impl.py#L1022)**

    ```python
    example = ManyFieldExample('alias', 1.0, 1, [], {}, [])
    ```

- **Conjure union: [UnionTypeExample](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/types/expected/package_name/_impl.py#L1465)**

    ```python
    stringVariant = UnionTypeExample(string_example="foo")
    ```

- **Conjure enum: [EnumExample](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/types/expected/package_name/_impl.py#L888)**

  ```python
  one = EnumExample.ONE;
  print(one); // prints: 'ONE'
  ```

- **Conjure alias: [StringAliasExample](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/types/expected/package_name/_impl.py#L1895)**

  Python uses structural (duck-typing) so aliases are currently transparent.

## Example Client interfaces
Example service interface: [TestService](https://github.com/palantir/conjure-python/blob/develop/conjure-python-core/src/test/resources/services/expected/package_name/_impl.py#L21)

```python
class another_TestService(Service):
    """
    A Markdown description of the service. "Might end with quotes"
    """

    def get_file_systems(self, auth_header):
        # type: (str) -> Dict[str, product_datasets_BackingFileSystem]
        """
        Returns a mapping from file system id to backing file system configuration.
        """

        _headers = {
            'Accept': 'application/json',
            'Authorization': auth_header,
        } # type: Dict[str, Any]

        _params = {
        } # type: Dict[str, Any]

        _path_params = {
        } # type: Dict[str, Any]

        _json = None # type: Any

        _path = '/catalog/fileSystems'
        _path = _path.format(**_path_params)

        _response = self._request( # type: ignore
            'GET',
            self._uri + _path,
            params=_params,
            headers=_headers,
            json=_json)

        _decoder = ConjureDecoder()
        return _decoder.decode(_response.json(), DictType(str, product_datasets_BackingFileSystem))
```

## Constructing clients

Use [conjure-python-client](https://github.com/palantir/conjure-python-client) which leverages [requests](http://docs.python-requests.org/en/master/):

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

## License
This project is made available under the [Apache 2.0 License](/LICENSE).
