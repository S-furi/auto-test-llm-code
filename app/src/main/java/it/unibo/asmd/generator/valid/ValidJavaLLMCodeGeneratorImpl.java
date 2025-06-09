package it.unibo.asmd.generator.valid;

import it.unibo.asmd.generator.JavaLLMCodeGenerator;
import it.unibo.asmd.generator.LLMCodeGenerator;

public class ValidJavaLLMCodeGeneratorImpl implements ValidLLMCodeGenerator {
    private final JavaCodeCheckerStrategy javaCheckStrategy;
    private final LLMCodeGenerator delegate;

    public ValidJavaLLMCodeGeneratorImpl(final JavaCodeCheckerStrategy javaCheckStrategy, final JavaLLMCodeGenerator delegate) {
        this.javaCheckStrategy = javaCheckStrategy;
        this.delegate = delegate;
    }

    @Override
    public String generateValidCodeFromPrompt() {
        return "";
    }

    @Override
    public void setPrePrompt() {
        this.delegate.setPrePrompt();
    }

    @Override
    public void setPrompt(final String prompt) {
        this.delegate.setPrompt(prompt);
    }

    @Override
    public String generateCodeFromPrompt() {
        return this.delegate.generateCodeFromPrompt();
    }

    @Override
    public void setAdditionalPrePrompt(final String additionalPrePrompt) {
        this.delegate.setAdditionalPrePrompt(additionalPrePrompt);
    }

    @Override
    public void setPostPrompt() {
        this.delegate.setPostPrompt();
    }
}
