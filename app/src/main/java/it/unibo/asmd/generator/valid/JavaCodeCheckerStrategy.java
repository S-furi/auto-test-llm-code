package it.unibo.asmd.generator.valid;

import java.util.List;
import java.util.Optional;

/**
 * Functional interface that encapsulate the logic
 * to assess whether a given string is valid java code
 * and can actually compile.
 */
public interface JavaCodeCheckerStrategy {
    /**
     *
     * @param code the input java code
     * @return {{@link Optional<String>}} empty if provided code compiles and does not produce errors, the yielded error otherwise.
     */
    Optional<List<String>> checkJavaCode(String className, String code);
}
