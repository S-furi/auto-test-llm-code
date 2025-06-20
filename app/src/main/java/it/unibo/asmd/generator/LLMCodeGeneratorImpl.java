package it.unibo.asmd.generator;

import it.unibo.asmd.generator.prompting.PromptBasedAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;

public class LLMCodeGeneratorImpl implements LLMCodeGenerator {
    private final List<String> prompts = new ArrayList<>();
    private Optional<String> userPrompt = Optional.empty();
    private PromptBasedAgent agent;
    protected final Pattern codeBlockPattern = Pattern.compile("```(\\w+)?\\s*([\\s\\S]*?)```");

    public LLMCodeGeneratorImpl(final boolean setPrePrompt, final PromptBasedAgent agent) {
        if (setPrePrompt) {
            this.setPrePrompt();
        }
        if (Objects.isNull(agent)) {
            throw new IllegalStateException("No AI agent is set!");
        }
        this.agent = agent;
    }

    @Override
    public void setPrePrompt() {
        final String prePrompt = "I will provide you with a set of specifications, and you will have to implement a java class that satisfies them. \n"
                + "Your answer MUST include code only, because your response will be directly compiled and executed. \n";
        this.prompts.add(prePrompt);
    }

    @Override
    public void setPrompt(final String prompt) {
        this.prompts.add(prompt);
        this.userPrompt = Optional.of(prompt);
    }

    @Override
    public String generateCodeFromPrompt() {
        return this.cleanUpResponse(this.computeResponse());
    }

    @Override
    public void setAdditionalPrePrompt(final String additionalPrePrompt) {
        this.prompts.add(additionalPrePrompt);
    }

    @Override
    public void setPostPrompt() {
        final String postPrompt = "Please provide java code only, without supplementary text (your code must be compiled right away)";
        this.prompts.add(postPrompt);
    }

    protected String computeResponse() {
        final String finalPrompt = this.prompts.stream().reduce("", (acc, prompt) -> acc + prompt + "\n");
        return this.agent.ask(finalPrompt);
    }

    private String cleanUpResponse(final String response) {
        final Matcher matcher = this.codeBlockPattern.matcher(response);
        final List<String> extractedBlocks = new ArrayList<>();

        while (matcher.find()) {
            final String language = matcher.group(1);
            final String code = matcher.group(2);

            System.out.println("Language: " + (language != null ? language : "None"));
            System.out.println("Code:\n" + code);
            extractedBlocks.add(code);
        }

        return extractedBlocks.get(0);
    }

    @Override
    public void clearPrompt() {
        this.prompts.clear();
        this.userPrompt = Optional.empty();
    }

    @Override
    public Optional<String> getPrompt() {
        return this.userPrompt;
    }
}
