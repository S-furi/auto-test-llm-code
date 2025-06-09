package it.unibo.asmd.generator;

import it.unibo.asmd.compiler.CodeCompiler;
import it.unibo.asmd.compiler.RuntimeCodeCompiler;
import it.unibo.asmd.generator.testing.LLMTestGenerator;
import it.unibo.asmd.generator.testing.LLMUnitTestGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTestGeneratorTest {

    private final String testClass = "public class Onner { public Onner() {} public int getOne() { return 1; } } ";
    private final String testClassName = "Onner";
    private final CodeCompiler codeComplier = new RuntimeCodeCompiler();
    private LLMTestGenerator testGenerator;

    @BeforeEach
    public void setUp() {
        final var baseCodeGenerator = (JavaLLMCodeGenerator) LLMCodeGeneratorFactory.createQwenLLMJavaCodeGenerator();
        this.testGenerator = new LLMUnitTestGenerator(LLMCodeGeneratorFactory.createValidLLMCodeGenerator(baseCodeGenerator, this.codeComplier));
    }

    @Test
    public void testGenerate() {
        this.codeComplier.dumpGeneratedCode(this.testClass, this.testClassName);
        this.codeComplier.compileGeneratedCode(this.testClassName);

        testGenerator.setSourceCode(this.testClass, this.testClassName);
        final var res = testGenerator.generateTest();
        System.out.println(res);
        assertNotEquals("", res);
    }

}
