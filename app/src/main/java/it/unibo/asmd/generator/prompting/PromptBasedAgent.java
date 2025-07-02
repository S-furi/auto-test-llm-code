package it.unibo.asmd.generator.prompting;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.Objects;

public class PromptBasedAgent {
    private final ChatLanguageModel model;

    public PromptBasedAgent(final ChatLanguageModel model) {
        this.model = model;
    }

    public String ask(final String prompt) {
        return this.model.chat(UserMessage.from(prompt)).aiMessage().text();
    }

    public static PromptBasedAgent createOllamaPromptBasedAgent(final String modelName) {
        final ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .logRequests(false)
                .logResponses(true)
                .modelName(modelName)
                .numPredict(256)
                .build();

        Objects.requireNonNull(model, "Model is null");
        return new PromptBasedAgent(model);
    }
}
