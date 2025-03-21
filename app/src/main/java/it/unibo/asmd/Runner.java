package it.unibo.asmd;

import it.unibo.asmd.compiler.DynamicCodeCompiler;
import it.unibo.asmd.generator.LLMCodeGenerator;

import java.lang.reflect.InvocationTargetException;

public class Runner {
    public static void main(String[] args) {
        final var generator = LLMCodeGenerator.createQwenLLMCodeGenerator();
        final var testCodeRequirements = "Write a class named \"Onner\" that has an empty constructor and a method named \"getOne\" that returns 1.";
        generator.setPrompt(testCodeRequirements);
        final var generatedCode = generator.generateCodeFromPrompt();
        System.out.println("Response:\n" + generatedCode);

        final var codeCompiler = new DynamicCodeCompiler();
        codeCompiler.dumpGeneratedCode(generatedCode, "Onner");
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
