package it.unibo.asmd.integration;

import it.unibo.asmd.compiler.CodeCompiler;
import it.unibo.asmd.compiler.RuntimeCodeCompiler;
import it.unibo.asmd.compiler.tests.DynamicTestRunner;
import it.unibo.asmd.generator.JavaLLMCodeGenerator;
import it.unibo.asmd.generator.LLMCodeGeneratorFactory;
import it.unibo.asmd.generator.testing.LLMTestGenerator;
import it.unibo.asmd.generator.testing.LLMUnitTestGenerator;
import it.unibo.asmd.generator.valid.ValidLLMCodeGenerator;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateCodeAndUnitTest {
    private final String className = "Counter";
    private final String testCodeRequirements = "Write a class named \"Counter\" that has an empty constructor and two methods: \"void inc()\" which increments the counter value, \"void dec()\" which decrement the value and, \"void reset()\" which reset the counter to 0 and a \"int getValue()\" method that return current counter value.";
    private final CodeCompiler codeCompiler = new RuntimeCodeCompiler();
    private final ValidLLMCodeGenerator codeGenerator = LLMCodeGeneratorFactory.createValidLLMCodeGenerator(
            (JavaLLMCodeGenerator) LLMCodeGeneratorFactory.createQwenLLMJavaCodeGenerator(),
            codeCompiler
    );

    private final LLMTestGenerator testGenerator = new LLMUnitTestGenerator(codeGenerator);

    private void dumpAndCompile(final String code, final String className) {
        codeCompiler.dumpGeneratedCode(code, className);
        codeCompiler.compileGeneratedCode(className);
    }

    private void generateSourceAndTest() {
        codeGenerator.setPrompt(this.testCodeRequirements);
        final String classCode = codeGenerator.generateValidCodeFromPrompt(className);

        dumpAndCompile(classCode, className);

        codeGenerator.clearPrompt();

        testGenerator.setSourceCode(classCode, className);
        final String testCode = testGenerator.generateTest();

        dumpAndCompile(testCode, className + "Test");
    }

    @Test
    public void generateCodeAndUnitTest() {
        generateSourceAndTest();

        final var loadedTest = codeCompiler.loadCompiledCode(this.className + "Test");
        var summary = DynamicTestRunner.runTestsOn(loadedTest.get().getClass());
        summary.printTo(new PrintWriter(System.out));
        assertEquals(0, summary.getTotalFailureCount());
    }
}
