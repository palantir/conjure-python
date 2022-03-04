# Contributing

The team welcomes contributions! To make changes:

- Fork the repo and make a branch
- Write your code (ideally with tests) and make sure the CircleCI build passes
- Open a PR

## Local development

### Prerequisites

- Java 8
- Python2 (On macOS: `brew install python3`)
- Python3 (On macOS: `brew install python`)
- [pipenv](https://github.com/pypa/pipenv) (`pip3 install pipenv`)


_We recommend the free [VSCode](https://code.visualstudio.com/) editor to work on python projects
and [Intellij IDEA Community Edition](https://www.jetbrains.com/idea/) for Java projects._

### One-time setup for development

1. Fork the repository
1. Generate the IDE configuration: `./gradlew idea`
1. Open projects in Intellij: `open *.ipr`
1. Generate integration test bindings: `./gradlew generate`
1. In `conjure-python-verifier/python`:

    ```shell
    $ PIPENV_VENV_IN_PROJECT=1 pipenv shell # create the virtual environment
    $ pipenv install --dev # install all dependencies
    ```

### Development tips

- run `./gradlew checkstyleMain checkstyleTest` locally to make sure your code conforms to the code-style.
- Use `tox` in `conjure-python-verifier/python` to run all tests

