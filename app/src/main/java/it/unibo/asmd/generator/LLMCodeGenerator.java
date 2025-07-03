package it.unibo.asmd.generator;

import it.unibo.asmd.generator.prompting.PromptBasedAgent;

import java.util.Optional;

public interface LLMCodeGenerator {
    /**
     * Sets a pre-prompt that ensures output to be valid and clean code.
     */
    void setPrePrompt();

    /**
     * Feeds this agent with the given prompt.
     *
     * @param prompt the request.
     */
    void setPrompt(String prompt);

    /**
     * Generates code from the current prompt. Be sure to set the prompt before calling this method
     * with {@link #setPrompt(String)}.
     * @return
     */
    String generateCodeFromPrompt();

    /**
     * Sets an additional pre-prompt that will be added to the current prompt.
     * @param additionalPrePrompt the additional pre-prompt to be added.
     */
    void setAdditionalPrePrompt(String additionalPrePrompt);

    /**
     * Sets a post-prompt that enforces output to be valid and clean code.
     */
    void setPostPrompt();

    /**
     * Cleans up the current prompt.
     */
    void clearPrompt();

    /**
     * Retrieves the current prompt without pre or post prompts.
     */
   Optional<String> getPrompt();

    static LLMCodeGenerator createSmollLLMCodeGenerator() {
        return new LLMCodeGeneratorImpl(true, PromptBasedAgent.createOllamaPromptBasedAgent("smollm"));
    }

    static LLMCodeGenerator createQwenLLMCodeGenerator() {
        return new LLMCodeGeneratorImpl(true, PromptBasedAgent.createOllamaPromptBasedAgent("qwen2.5:3b"));
    }

}
