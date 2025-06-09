package it.unibo.asmd.generator;

import it.unibo.asmd.generator.prompting.PromptBasedAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class JavaLLMCodeGeneratorImpl implements JavaLLMCodeGenerator {
    private LLMCodeGeneratorImpl codeGenerator;

    public JavaLLMCodeGeneratorImpl(final boolean setPrePrompt, final PromptBasedAgent agent) {
        this.codeGenerator = new LLMCodeGeneratorImpl(setPrePrompt, agent);
    }

    @Override
    public void setPrePrompt() {
        this.codeGenerator.setPrePrompt();
    }

    @Override
    public void setPrompt(final String prompt) {
        this.codeGenerator.setPrompt(prompt);
    }

    @Override
    public String generateCodeFromPrompt() {
        final var raw = this.codeGenerator.computeResponse();
        return this.cleanUpResponse(raw);
    }

    @Override
    public void setAdditionalPrePrompt(final String additionalPrePrompt) {
        this.codeGenerator.setAdditionalPrePrompt(additionalPrePrompt);
    }

    @Override
    public void setPostPrompt() {
        this.codeGenerator.setPostPrompt();
    }

    @Override
    public String cleanUpResponse(String rawResponse) {
        final Matcher matcher = this.codeGenerator.codeBlockPattern.matcher(rawResponse);
        final List<String> extractedBlocks = new ArrayList<>();

        while (matcher.find()) {
            final String lang = matcher.group(1);
            final String code = matcher.group(2);

            if (lang == null || lang.equals("java")) {
                extractedBlocks.add(code);
            } else {
                throw new IllegalStateException(String.format("LLM Generated code is not in Java (got: %s): %s", lang, code));
            }
        }
        // TODO: check which approach is best to fuse code blocks
        return String.join("\n", extractedBlocks);
    }
}
