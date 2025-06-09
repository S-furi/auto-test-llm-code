package it.unibo.asmd.generator;

import it.unibo.asmd.generator.prompting.PromptBasedAgent;

import java.util.Optional;

public interface LLMCodeGenerator {
    void setPrePrompt();

    void setPrompt(String prompt);

    String generateCodeFromPrompt();

    void setAdditionalPrePrompt(String additionalPrePrompt);

    void setPostPrompt();

    static LLMCodeGenerator createSmollLLMCodeGenerator() {
        return new LLMCodeGeneratorImpl(true, PromptBasedAgent.createOllamaPromptBasedAgent("smollm"));
    }

    static LLMCodeGenerator createQwenLLMCodeGenerator() {
        return new LLMCodeGeneratorImpl(true, PromptBasedAgent.createOllamaPromptBasedAgent("qwen2.5:3b"));
    }

    void clearPrompt();

    /**
     * Retrieves the current prompt without pre or post prompts.
     */
   Optional<String> getPrompt();
}
