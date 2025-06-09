package it.unibo.asmd.generator.testing;

import it.unibo.asmd.generator.valid.ValidLLMCodeGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class LLMUnitTestGenerator implements LLMTestGenerator {

    private final ValidLLMCodeGenerator codeGenerator;
    private String code;
    private String className;

    public LLMUnitTestGenerator(final ValidLLMCodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void setSourceCode(final String sourceCode, final String className) {
        this.code = sourceCode;
        this.className = className + "Test";
    }

    @Override
    public void setSourceCode(final File sourceCode, final String className) {
        try (final var lines = Files.lines(sourceCode.toPath())) {
            this.code = lines.collect(Collectors.joining());
            this.className = className + "Test";
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateTest() {
        final var prePrompt = "Consider the following Java class called `" + this.className + "`:\n" + this.code;
        final var postPrompt = "\n Given the above class, write a unit test Using JUnit5, including also all necessary imports.";

        this.codeGenerator.setPrompt(prePrompt + postPrompt);
        this.codeGenerator.setPostPrompt(); // this should help generating only (one) code block in the response
        return this.codeGenerator.generateValidCodeFromPrompt(this.className);
    }
}
