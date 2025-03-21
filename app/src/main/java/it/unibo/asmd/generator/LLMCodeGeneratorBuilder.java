package it.unibo.asmd.generator;

import java.util.Optional;

public class LLMCodeGeneratorBuilder {

    private Optional<LLMCodeGenerator> codeGenerator = Optional.empty();
    private Optional<PromptBasedAgent> agent = Optional.empty();
    private boolean defaultPrePrompt = false;
    private String additionalPrePrompt = "";
    private String postPrompt = "";
    private Optional<Language> lang = Optional.empty();

    private boolean built = false;

    public LLMCodeGeneratorBuilder() {}

    public LLMCodeGeneratorBuilder withDefaultPrePrompt() {
        this.defaultPrePrompt = true;
        return this;
    }

    public LLMCodeGeneratorBuilder ofLanguage(final Language lang) {
        this.lang = Optional.of(lang);
        return this;
    }

    public LLMCodeGeneratorBuilder withPrePrompt(final String additionalPrePrompt) {
        this.additionalPrePrompt = additionalPrePrompt;
        return this;
    }

    public LLMCodeGeneratorBuilder withPostPrompt(final String postPrompt) {
        this.postPrompt = postPrompt;
        return this;
    }

    public LLMCodeGeneratorBuilder withAgent(final PromptBasedAgent agent) {
        this.agent = Optional.of(agent);
        return this;
    }

    public LLMCodeGeneratorBuilder withSmollmAgent() {
        return this.withAgent(PromptBasedAgent.createOllamaPromptBasedAgent("smollm"));
    }

    public LLMCodeGeneratorBuilder withQwenAgent() {
        return this.withAgent(PromptBasedAgent.createOllamaPromptBasedAgent("qwen2.5:3b"));
    }

    public LLMCodeGenerator build() {
        if (this.built) {
            throw new IllegalStateException("This builder has already been built");
        }
        if (this.agent.isEmpty()) {
            throw new IllegalStateException("No AI agent is set!");
        }
        this.built = true;

        if (this.lang.isPresent()) {
            switch (this.lang.get()) {
                case JAVA -> this.codeGenerator = Optional.of(new JavaLLMCodeGenerator(this.defaultPrePrompt, this.agent.get()));
                default -> this.codeGenerator = Optional.of(new LLMCodeGeneratorImpl(this.defaultPrePrompt, this.agent.get()));
            }
        }

        if (!this.additionalPrePrompt.isEmpty()) {
            this.codeGenerator.get().setAdditionalPrePrompt(this.additionalPrePrompt);
        }
        if (!this.postPrompt.isEmpty()) {
            this.codeGenerator.get().setPostPrompt();
        }

        return this.codeGenerator.get();
    }

    public enum Language {
        JAVA,
    }
}
