# Contributing

The team welcomes contributions! To make changes:

- Fork the repo and make a branch
- Write your code (ideally with tests) and make sure the CircleCI build passes
- Open a PR

## Local development

### Prerequisites

- Java 11
- Python3 (On macOS: `brew install python`)
- [pipenv](https://github.com/pypa/pipenv) (`pip3 install pipenv`)

_We recommend [VS Code](https://code.visualstudio.com/) to work on python projects
and [Intellij IDEA Community Edition](https://www.jetbrains.com/idea/) for Java projects._

### One-time setup for development

1. Fork the repository
2. Generate the IDE configuration: `./gradlew idea`
3. Open projects in Intellij: `open *.ipr`
4. Generate integration test bindings: `./gradlew generate`
5. In `conjure-python-verifier/python`:

    ```shell
    $ PIPENV_VENV_IN_PROJECT=1 pipenv shell # create the virtual environment
    $ pipenv install --dev # install all dependencies
    ```

### Development tips

- Run `./gradlew checkstyleMain checkstyleTest` locally to make sure your code conforms to the code-style.
- Run `pipenv run tox` in `conjure-python-verifier/python` to run all Python tests.

