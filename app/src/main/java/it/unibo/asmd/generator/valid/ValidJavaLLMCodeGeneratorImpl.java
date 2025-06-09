package it.unibo.asmd.generator.valid;

import it.unibo.asmd.generator.JavaLLMCodeGenerator;
import it.unibo.asmd.generator.LLMCodeGenerator;

import java.util.Optional;

public class ValidJavaLLMCodeGeneratorImpl implements ValidLLMCodeGenerator {
    private final JavaCodeCheckerStrategy javaCheckStrategy;
    private final LLMCodeGenerator delegate;
    public static final int DEFAULT_MAX_RETRIES = 5;
    private int maxRetries;

    public ValidJavaLLMCodeGeneratorImpl(final JavaCodeCheckerStrategy javaCheckStrategy, final JavaLLMCodeGenerator delegate) {
        this.javaCheckStrategy = javaCheckStrategy;
        this.delegate = delegate;
        this.maxRetries = DEFAULT_MAX_RETRIES;
    }

    @Override
    public String generateValidCodeFromPrompt() {
        int tries = 0;
        do {
            final var code = this.delegate.generateCodeFromPrompt();
            final var errors = this.javaCheckStrategy.checkJavaCode(code);
            if (errors.isEmpty()) {
                return code;
            }
            tries++;
            var userPrompt = this.delegate.getPrompt().orElseThrow();
            this.delegate.clearPrompt();
            this.buildFixPrompt(userPrompt, errors.get());
        } while (tries < this.maxRetries);
        throw new IllegalStateException("LLM could not provide valid java code in " + this.maxRetries + " retries");
    }

    private void buildFixPrompt(final String userPrompt, final Iterable<String> errors) {
        final String errorsPrompt = String.join("\n- ", errors);
        this.delegate.setAdditionalPrePrompt("Consider the following set of errors: " + errorsPrompt);
        this.delegate.setAdditionalPrePrompt("Given the set of errors, fix the following code:\n");
        this.delegate.setPrompt(userPrompt);

        /* this should ensure that the LLM just outputs code that should run, avoiding
         * producing undesirable stuff like re-proposing the error and later the solution. */
        this.delegate.setPostPrompt();
    }

    @Override
    public void setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
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

    @Override
    public void clearPrompt() {
        this.delegate.clearPrompt();
    }

    @Override
    public Optional<String> getPrompt() {
        return this.delegate.getPrompt();
    }
}
