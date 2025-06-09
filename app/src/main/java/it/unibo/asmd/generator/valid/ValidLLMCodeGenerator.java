package it.unibo.asmd.generator.valid;

import it.unibo.asmd.generator.LLMCodeGenerator;

/**
 * {{@link LLMCodeGenerator}} that enforces the production of valid java code,
 * checking its validity upon creation.
 */
public interface ValidLLMCodeGenerator extends LLMCodeGenerator {
    String generateValidCodeFromPrompt();

    /**
     * Set the maximum tries this agents tries to retrieve a solution.
     *
     * @param maxRetries how many times the agent retries to reproduce code.
     */
    void setMaxRetries(final int maxRetries);
}
