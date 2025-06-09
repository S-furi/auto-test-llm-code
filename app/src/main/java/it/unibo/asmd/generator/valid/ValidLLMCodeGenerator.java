package it.unibo.asmd.generator.valid;

import it.unibo.asmd.generator.LLMCodeGenerator;

import java.util.List;
import java.util.Optional;

/**
 * {{@link LLMCodeGenerator}} that enforces the production of valid java code,
 * checking its validity upon creation.
 */
public interface ValidLLMCodeGenerator extends LLMCodeGenerator {
    String generateValidCodeFromPrompt(String className);

    /**
     * Cheks whether the input code is valid and semantically correct Java.
     *
     * @param className the name of the input class file
     * @param code the actual code to test
     * @return an {{@link Optional<List<String>>}} empty if the code is correct, a list of errors otherwise.
     */
    Optional<List<String>> validateCode(String className, String code);

    /**
     * Set the maximum tries this agents tries to retrieve a solution.
     *
     * @param maxRetries how many times the agent retries to reproduce code.
     */
    void setMaxRetries(final int maxRetries);
}
