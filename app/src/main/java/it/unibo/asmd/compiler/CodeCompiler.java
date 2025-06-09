package it.unibo.asmd.compiler;

import java.util.Optional;

public interface CodeCompiler {
    /**
     * Creates a java source file with the provided code. No checks are performed
     * whether the provided code is valid java code or semantically correct.
     * The file will be saved in this project resources directory under the
     * "/generated" sources.
     *
     * @param generatedCode the provided java code.
     * @param className the provided java source class name.
     * @return true if code is saved correctly to a file, false otherwise.
     */
    boolean dumpGeneratedCode(String generatedCode, String className);

    /**
     * Load and compile given className which must match a valid java file.
     * By default the java file is searched into "/generated" resources.
     *
     * @param className the classname of the java sources.
     * @return true if the code is compiled successfully producing its associated .class file, false otherwise.
     */
    boolean compileGeneratedCode(String className);

    Optional<Object> loadCompiledCode(String classPath);

    /**
     * Checks whether the given code is valid java code and can actually compile.
     *
     * @param classname the provided java source class name.
     * @param code the java code to be checked.
     * @return true if the code is valid java (and can compile), false otherwise
     */
    boolean canCompile(String classname, String code);
}
