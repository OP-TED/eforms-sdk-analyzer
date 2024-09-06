# eForms SDK Analyzer

The eForms SDK Analyzer is a command-line application for the static analysis of the content of the eForms SDK.
It loads the files from the eForms SDK, and applies various checks and verifications, to try to ensure that their content is correct and consistent.

It can also run a benchmark of the Schematron rules in an eForms SDK, to help detect potential performance issues in those validation rules.

## Building

Requirements:

* Java 11 or higher
* Maven 3.8, other versions probably also work

Run the following in the root folder of this project to create a runnable JAR of the application:

```shell
mvn clean package
```

## Usage

### SDK Analysis

To analyze the content of an eForms SDK, execute the runnable JAR built as described above, indicating the path to the folder containing the eForms SDK:

```shell
java -jar target/eforms-sdk-analyzer-1.8.0-SNAPSHOT.jar path/to/eforms-sdk
```

This will return the exit code 0 if no errors are found, and 1 otherwise.

Any error or warning found during the analysis will be logged at the corresponding level. By default, logs go to the standard output.

### Schematron rules benchmark

To run the benchmark of the Schematron rules in an eForms SDK, execute the runnable JAR built as described above, indicating the path to the folder containing the eForms SDK and the command `benchmark`:

```shell
java -jar target/eforms-sdk-analyzer-1.8.0-SNAPSHOT.jar path/to/eforms-sdk benchmark
```

This will execute the Schematron rules on a few large XML notices included as resources in this project, with an appropriate warm-up and number of iterations. The results are written to a JSON file.

You can compare those results with the ones from another SDK version, to detect whether the rules have become slower to execute.

## Contributing

### Adding a check

The analysis is done by various classes that implement the `Validator` interface.

The bulk of the checks is done by `SdkValidator`, which uses the [Drools rule engine](https://www.drools.org/) to apply the set of rules defined in `src\main\resources\eu\europa\ted\eforms\sdk\analysis\drools`.

### Unit tests

Each check should have corresponding unit tests, with both valid and invalid test data.

Those tests are defined using [Cucumber](https://cucumber.io/).

In order to run tests for only one Cucumber feature, you can filter based on the tag indicated at the top of the feature file:

```shell
 mvn test -Dcucumber.filter.tags="@tedefo-2447"
```

Unit tests for other parts of the application can be defined as regular JUnit tests.

## Licence

Copyright 2022 European Union

_Licensed under the EUPL, Version 1.2 or – as soon they will be approved by the European Commission –
subsequent versions of the EUPL (the "Licence");_
_You may not use this work except in compliance with the Licence. You may obtain [a copy of the Licence here](LICENSE)._  
_Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the Licence for the specific language governing permissions and limitations under the Licence._
