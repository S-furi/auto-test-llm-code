package it.unibo.asmd.generator;

import it.unibo.asmd.compiler.CodeCompiler;
import it.unibo.asmd.compiler.RuntimeCodeCompiler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class LLMCodeGeneratorTest {
    private final CodeCompiler codeCompiler = new RuntimeCodeCompiler();
    private final String testCodeRequirements = "Write a class named \"Onner\" that has an empty constructor and a method named \"getOne\" that returns 1.";

    private String getSimplePrompt() {
        final var generator = LLMCodeGeneratorFactory.createQwenLLMJavaCodeGenerator();
        generator.setPrompt(testCodeRequirements);
        final var generatedCode = generator.generateCodeFromPrompt();
        System.out.println("Response:\n" + generatedCode);
        return generatedCode;
    }

    private String getValidPrompt() {
        final var base = (JavaLLMCodeGenerator) LLMCodeGeneratorFactory.createQwenLLMJavaCodeGenerator();
        final var generator = LLMCodeGeneratorFactory.createValidLLMCodeGenerator(base, codeCompiler);
        generator.setPrompt(testCodeRequirements);
        final var generatedCode = generator.generateValidCodeFromPrompt("Onner");
        System.out.println("Response:\n" + generatedCode);
        return generatedCode;
    }

    public Object compileGeneratedCode(final String generatedCode, final String className) {
        codeCompiler.dumpGeneratedCode(generatedCode, className);
        codeCompiler.compileGeneratedCode(className);
        final var obj = codeCompiler.loadCompiledCode(className);
        if (obj.isEmpty()) {
            System.out.println("No compiled code, an error occurred...");
            System.exit(1);
        }
        return obj.get();
    }

    private void testOnner(final Object onner) {
        try {
            final var getOne = onner.getClass().getDeclaredMethod("getOne");
            final int res = (int)getOne.invoke(onner);
            if (res != 1) {
                System.out.println("The compiled code did not return the expected result...");
                fail();
            }
        } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("An error occurred when invoking the compiled code...");
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testSimpleLLMCodeGenerator() {
        final var generatedCode = getSimplePrompt();
        var onner = compileGeneratedCode(generatedCode, "Onner");
        testOnner(onner);
    }

    @Test
    void testValidLLMCodeGenerator() {
        final var generatedCode = getValidPrompt();
        var onner = compileGeneratedCode(generatedCode, "Onner");
        testOnner(onner);
    }
}
