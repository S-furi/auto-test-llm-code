package it.unibo.asmd.compiler;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DynamicCodeCompilerTest {
    @Test
    void testCompiledCode() {
        final String testCode = "public class Test { public Test() { } public int getOne() { return 1; } }";
        final String className = "Test";
        final DynamicCodeCompiler compiler = new DynamicCodeCompiler();
        if (compiler.dumpGeneratedCode(testCode, className)) {
            if (compiler.compileGeneratedCode(className)) {
                final Optional<Object> obj = compiler.loadCompiledCode(className);
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
}
