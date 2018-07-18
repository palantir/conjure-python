# Conjure-Python

_Generate python classes for interacting with [Conjure](https://github.com/palantir/conjure) defined APIs._

## Usage

The recommended way to use conjure-python is via a build tool like [gradle-conjure](https://github.com/palantir/gradle-conjure). However, if you don't want to use gradle-conjure, there is also an executable which conforms to [RFC 002](https://github.com/palantir/conjure/blob/develop/rfc/002-contract-for-conjure-generators.md).

    Usage: conjure-python generate <input> <output> [...options]

        --packageName         package name that will appear in setup.py
        --packageVersion      version number that will appear in setup.py
        --packageDescription  description that will appear in setup.py
        --packageUrl          url that will appear in setup.py
        --packageAuthor       author that will appear in setup.py
        --writeCondaRecipe    use this boolean option to generate a `conda_recipe/meta.yaml`

## Python 2 and 3 compatible

conjure-python generates code that works on both Python 2 and Python 3.

## Generated services

- [TestService](./conjure-python-core/src/test/resources/services/expected/package/another/__init__.py)

```python
from httpremoting import RequestsClient, ServiceConfiguration

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
