# Contributing

The team welcomes contributions! To make changes:

- Fork the repo and make a branch
- Write your code (ideally with tests) and make sure the CircleCI build passes
- Open a PR

## Local development

### Prerequisites

The generator is written in Java, so you'll need Java 8 installed on your machine. (We recommend [Intellij IDEA Community Edition](https://www.jetbrains.com/idea/) for Java projects.)

### One-time setup for development

1. Fork the repository
1. Generate the IDE configuration: `./gradlew idea`
1. Open projects in Intellij: `open *.ipr`

### Development tips

- run `./gradlew checkstyleMain checkstyleTest` locally to make sure your code conforms to the code-style.

