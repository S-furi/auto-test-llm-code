package it.unibo.asmd.compiler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicCodeCompilerTest {
    private final String testCode = "public class Test { public Test() { } public int getOne() { return 1; } }";
    private final String className = "Test";

    private DynamicCodeCompiler compiler;

    @BeforeEach
    public void setUp() {
        this.compiler = new DynamicCodeCompiler();
    }

    @Test
    void testCompiledCode() {
        if (this.compiler.dumpGeneratedCode(this.testCode, this.className)) {
            if (this.compiler.compileGeneratedCode(this.className)) {
                final Optional<Object> obj = this.compiler.loadCompiledCode(this.className);
                obj.ifPresent(o -> {
                    try {
                        final int res = (int) o.getClass().getMethod("getOne").invoke(o);
                        assertEquals(1, res);
                    } catch (final Exception e) {
                        e.printStackTrace();
                        fail();
                    }
                });
            } else {
                fail();
            }
        } else {
            fail();
        }
    }

    @Test
    void testCanCompile() {
        assertTrue(this.compiler.canCompile(this.className, this.testCode));
        assertFalse(this.compiler.canCompile(this.className, "fun addOne(x: Int): Int = x + 1"));
    }
}
