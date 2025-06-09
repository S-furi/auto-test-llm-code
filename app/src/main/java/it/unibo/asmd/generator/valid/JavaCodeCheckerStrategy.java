package it.unibo.asmd.generator.valid;

/**
 * Functional interface that encapsulate the logic
 * to assess whether a given string is valid java code
 * and can actually compile.
 */
public interface JavaCodeCheckerStrategy {
    /**
     *
     * @param code the input java code
     * @return true if provided java code is correct (i.e. it compiles), false otherwise.
     */
    boolean checkJavaCode(String code);
}
