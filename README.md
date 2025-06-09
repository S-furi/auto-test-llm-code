# Auto Test LLM Code

A simple java application for evaluating simple LLM-generated Java code, leveraging Ollama and Java's Reflections API.

The project AIMs in producing a simple interface in order to give a set of specification in natural langauge for a software component, represented
as a single Java class (by now only single files are supported). Generated code is checked by means of the Java compiler, assessing
code correctness both syntactivally and semantically. Once code is correct, it could be dumped in the FileSystem.
Another component is responsible of generating Unit tests by levearing JUnit5 as a test suite. The generated code is given to
such class, which produces a set of unit tests, always checking the results by means of the java compiler (as done before).

As shown in [`GenerateCodeAndUnitTest`](https://github.com/S-furi/auto-test-llm-code/blob/main/app/src/test/java/it/unibo/asmd/integration/GenerateCodeAndUnitTest.java) it is possible to define a **pipeline** in order to, from a given prompt, generate correct Java code,
generate a set of correct Unit Tests and run them upon the generated code.
This pipeline ensures a quite robust approach in genrating LLM code, because we have ways to ensure if it compiles and if it does withstand
a set of unit tests (which should guarantee and mirror user's requirements), all of this in an automatic fashion.

## Further Considerations

In the following sections some further considerations and possibile future ideas are illustrated.

### TDD
By now, we generate tests from initial produced code. If we would leverage TDD in a strictier way, we should firstly present requirements
and generate tests (which will be then harder to automatically run and assess if they compile if we do not genrate in the same time the
required dependencies). Only when correct tests that mirrors user's requirements are generated, we can then proceed in generating code
that (1) must compile and (2) must make all tests pass.
It would be interesting in investigating performance differences betweenn these two approaches (strict TDD or code + subsequent tests).

### RAG and Formal Requirements
It could be beneficial for LLM agents to produce more formal requirements starting from user's natural langauge specifications,
like converting user input into IEEE Guide for Software Requirements Specifications. This can then be used in two ways:

1. Including these requiements directly into the prompt, which will be repeated every time some provided code does not compile (when LLM generated code does not compile, a new prompt with errors and old prompt is proposed to the LLM in order to produce a solution);
2. Implement a RAG mechanism in order to encode requirements as a sort of memory for the agent.

Both ways should require further investigation, even if for larger projects and agents with limited context windows, the second
approach is beneficial even on paper.
