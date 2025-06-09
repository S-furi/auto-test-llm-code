package it.unibo.asmd.generator.valid;

import it.unibo.asmd.compiler.CodeCompiler;

public class RuntimeCompilerJavaCheckStrategy {
    public static JavaCodeCheckerStrategy getStrategy(final CodeCompiler compiler) {
        return code ->
            compiler.canCompile("", code).map(diagnostics ->
                    diagnostics.stream().map(diagnostic ->
                            "Error in line " + diagnostic.getLineNumber() + ": " + diagnostic.getMessage(null)
                    ).toList()
            );
    }
}
