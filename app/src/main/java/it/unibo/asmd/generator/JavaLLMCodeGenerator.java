package it.unibo.asmd.generator;

public interface JavaLLMCodeGenerator extends LLMCodeGenerator {

    /**
     * Clean up the response from the LLM to ensure it is valid Java code.
     * @param rawResponse the raw response from the LLM, which may contain additional text or formatting
     * @return a cleaned-up version of the response that is valid Java code
     */
    String cleanUpResponse(String rawResponse);

}
