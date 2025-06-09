package it.unibo.asmd;

import it.unibo.asmd.compiler.RuntimeCodeCompiler;
import it.unibo.asmd.generator.LLMCodeGeneratorFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class Runner {
    private static String getSimplePrompt() {
        final var generator = LLMCodeGeneratorFactory.createQwenLLMJavaCodeGenerator();
        final var testCodeRequirements = "Write a class named \"Onner\" that has an empty constructor and a method named \"getOne\" that returns 1.";
        generator.setPrompt(testCodeRequirements);
        final var generatedCode = generator.generateCodeFromPrompt();
        System.out.println("Response:\n" + generatedCode);
        return generatedCode;
    }

    public static Object compileGeneratedCode(final String generatedCode, final String className) {
        final var codeCompiler = new RuntimeCodeCompiler();
        codeCompiler.dumpGeneratedCode(generatedCode, className);
        codeCompiler.compileGeneratedCode(className);
        final var obj = codeCompiler.loadCompiledCode(className);
        if (obj.isEmpty()) {
            System.out.println("No compiled code, an error occurred...");
            System.exit(1);
        }
        return obj.get();
    }

    public static void main(String[] args) {
        final var generatedCode = getSimplePrompt();

        try {
            final var onner = compileGeneratedCode(generatedCode, "Onner");
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
