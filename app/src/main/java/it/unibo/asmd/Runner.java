package it.unibo.asmd;

import it.unibo.asmd.compiler.CodeCompiler;
import it.unibo.asmd.compiler.RuntimeCodeCompiler;
import it.unibo.asmd.generator.JavaLLMCodeGenerator;
import it.unibo.asmd.generator.LLMCodeGenerator;
import it.unibo.asmd.generator.LLMCodeGeneratorFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class Runner {
    private static final CodeCompiler codeCompiler = new RuntimeCodeCompiler();
    private static final String testCodeRequirements = "Write a class named \"Onner\" that has an empty constructor and a method named \"getOne\" that returns 1.";

    private static String getSimplePrompt() {
        final var generator = LLMCodeGeneratorFactory.createQwenLLMJavaCodeGenerator();
        generator.setPrompt(testCodeRequirements);
        final var generatedCode = generator.generateCodeFromPrompt();
        System.out.println("Response:\n" + generatedCode);
        return generatedCode;
    }

    private static String getValidPrompt() {
        final var base = (JavaLLMCodeGenerator) LLMCodeGeneratorFactory.createQwenLLMJavaCodeGenerator();
        final var generator = LLMCodeGeneratorFactory.createValidLLMCodeGenerator(base, codeCompiler);
        generator.setPrompt(testCodeRequirements);
        final var generatedCode = generator.generateValidCodeFromPrompt("Onner");
        System.out.println("Response:\n" + generatedCode);
        return generatedCode;
    }

    public static Object compileGeneratedCode(final String generatedCode, final String className) {
        codeCompiler.dumpGeneratedCode(generatedCode, className);
        codeCompiler.compileGeneratedCode(className);
        final var obj = codeCompiler.loadCompiledCode(className);
        if (obj.isEmpty()) {
            System.out.println("No compiled code, an error occurred...");
            System.exit(1);
        }
        return obj.get();
    }

    private static void testOnner(final Object onner) {
        try {
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
    }

    public static void main(String[] args) {
        final var generatedCode = getSimplePrompt();
        var onner = compileGeneratedCode(generatedCode, "Onner");
        testOnner(onner);
        System.out.println("Done! Simple LLM generated code that works and behaves as expected!");

        final var validCode = getValidPrompt();
        onner = compileGeneratedCode(validCode, "Onner");
        System.out.println("Done! Valid LLM generated code that works and behaves as expected!");
    }
}
