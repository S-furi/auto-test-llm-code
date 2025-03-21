package it.unibo.asmd.generator;

public class LLMCodeGeneratorFactory {
    public static LLMCodeGenerator createSmollLLMCodeGenerator() {
        return new LLMCodeGeneratorBuilder()
                .withSmollmAgent()
                .withDefaultPrePrompt()
                .build();
    }

    public static LLMCodeGenerator createQwenLLMCodeGenerator() {
        return new LLMCodeGeneratorBuilder()
                .withQwenAgent()
                .withDefaultPrePrompt()
                .build();
    }

    public static LLMCodeGenerator createSmollLLMJavaCodeGenerator() {
        return new LLMCodeGeneratorBuilder()
                .withSmollmAgent()
                .withDefaultPrePrompt()
                .ofLanguage(LLMCodeGeneratorBuilder.Language.JAVA)
                .build();
    }
}
