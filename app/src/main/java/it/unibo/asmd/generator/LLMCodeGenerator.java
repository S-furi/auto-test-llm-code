package it.unibo.asmd.generator;

import dev.langchain4j.model.ollama.OllamaChatModel;
import it.unibo.asmd.compiler.DynamicCodeCompiler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;

public class LLMCodeGenerator {
    private final List<String> prompts = new ArrayList<>();
    private PromptBasedAgent agent;

    public LLMCodeGenerator(final boolean setPrePrompt, final PromptBasedAgent agent) {
        if (setPrePrompt) {
            this.setPrePrompt();
        }
        if (Objects.isNull(agent)) {
            throw new IllegalStateException("No AI agent is set!");
        }
        this.agent = agent;
    }

    public void setPrePrompt() {
        final String prePrompt = "I will provide you with a set of specifications, and you will have to implement a java class that satisfies them. \n"
                + "Your answer MUST include code only, because your response will be directly compiled and executed. \n";
        this.prompts.add(prePrompt);
    }

    public void setAdditionalPrePrompt(final String additionalPrePrompt) {
        this.prompts.add(additionalPrePrompt);
    }

    public void setPrompt(final String prompt) {
        this.prompts.add(prompt);

    }

    public void setPostPrompt() {
        final String postPrompt = "Please provide java code only, without supplementary text (your code must be compiled right away)";
        this.prompts.add(postPrompt);
    }

    public String computeResponse() {
        final String finalPrompt = this.prompts.stream().reduce("", (acc, prompt) -> acc + prompt + "\n");
        return this.agent.ask(finalPrompt);
    }

    static LLMCodeGenerator createSmollLLMCodeGenerator() {
        return new LLMCodeGenerator(true, PromptBasedAgent.createOllamaPromptBasedAgent("smollm"));
    }

    public String cleanUpResponse(final String response) {
        // Regular expression to capture code blocks
        final Pattern pattern = Pattern.compile("```(\\w+)?\\s*([\\s\\S]*?)```");
        final Matcher matcher = pattern.matcher(response);

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

    static LLMCodeGenerator createQwenLLMCodeGenerator() {
        return new LLMCodeGenerator(true, PromptBasedAgent.createOllamaPromptBasedAgent("qwen2.5:3b"));
    }

    public static void main(String[] args) {
        final var generator = LLMCodeGenerator.createQwenLLMCodeGenerator();
        final var testCodeRequirements = "Write a class named \"Onner\" that has an empty constructor and a method named \"getOne\" that returns 1.";
        generator.setPrompt(testCodeRequirements);
        final var response = generator.computeResponse();
        final var cleanedResponse = generator.cleanUpResponse(response);
        System.out.println("Response:\n" + cleanedResponse);

        final var codeCompiler = new DynamicCodeCompiler();
        codeCompiler.dumpGeneratedCode(cleanedResponse, "Onner");
        codeCompiler.compileGeneratedCode("Onner");
        final var obj = codeCompiler.loadCompiledCode("Onner");
        if (obj.isEmpty()) {
            System.out.println("No compiled code, an error occurred...");
            System.exit(1);
        }
        try {
            final var onner = obj.get();
            final var getOne = onner.getClass().getDeclaredMethod("getOne");
            final int res = (int)getOne.invoke(onner);
            if (res != 1) {
                System.out.println("The compiled code did not return the expected result...");
                System.exit(1);
            }
        } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("An error occurred when invoking the compiled code...");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Done! LLM generated code has been a success");
    }
}
