package it.unibo.asmd.generator.testing;

import java.io.File;

public interface LLMTestGenerator {
    /**
     * Set source code upon which tests will be generated
     *
     * @param sourceCode as a string;
     */
    void setSourceCode(String sourceCode, String className);

    /**
     * Set source code upon which tests will be generated
     *
     * @param sourceCode as a {{@link File}}
     */
    void setSourceCode(File sourceCode, String className);

    String generateTest();
}
