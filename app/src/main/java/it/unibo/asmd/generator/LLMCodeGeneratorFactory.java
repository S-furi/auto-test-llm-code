package it.unibo.asmd.generator;

public class LLMCodeGeneratorFactory {
    public static LLMCodeGenerator createSmollLLMCodeGenerator() {
        return new LLMCodeGeneratorBuilder().withSmollmAgent().withDefaultPrePrompt().build();
    }
}
