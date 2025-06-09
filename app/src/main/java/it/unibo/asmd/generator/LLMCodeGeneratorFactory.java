package it.unibo.asmd.generator;

import it.unibo.asmd.compiler.CodeCompiler;
import it.unibo.asmd.generator.valid.RuntimeCompilerJavaCheckStrategy;
import it.unibo.asmd.generator.valid.ValidJavaLLMCodeGeneratorImpl;
import it.unibo.asmd.generator.valid.ValidLLMCodeGenerator;

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

    public static LLMCodeGenerator createQwenLLMJavaCodeGenerator() {
        return new LLMCodeGeneratorBuilder()
                .withQwenAgent()
                .withDefaultPrePrompt()
                .ofLanguage(LLMCodeGeneratorBuilder.Language.JAVA)
                .build();
    }

    public static ValidLLMCodeGenerator createValidLLMCodeGenerator(
            final JavaLLMCodeGenerator codeGenerator,
            final CodeCompiler compiler
    ) {
        return new ValidJavaLLMCodeGeneratorImpl(
                RuntimeCompilerJavaCheckStrategy.getStrategy(compiler),
                codeGenerator
        );
    }
}
