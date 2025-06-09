package it.unibo.asmd.generator;

public interface JavaLLMCodeGenerator extends LLMCodeGenerator {

    String cleanUpResponse(String rawResponse);

}
