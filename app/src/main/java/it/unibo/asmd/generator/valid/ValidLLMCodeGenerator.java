package it.unibo.asmd.generator.valid;

import it.unibo.asmd.generator.LLMCodeGenerator;

/**
 * {{@link LLMCodeGenerator}} that enforces the production of valid java code,
 * checking its validity upon creation.
 */
public interface ValidLLMCodeGenerator extends LLMCodeGenerator {
    String generateValidCodeFromPrompt();
}
