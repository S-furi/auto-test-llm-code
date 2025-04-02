package it.unibo.asmd.rag;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import it.unibo.asmd.generator.prompting.PromptBasedAgent;

import javax.annotation.Nullable;

public class RAGAgent extends PromptBasedAgent {
    private final Knowledge knowledge;
    private final KnowledgeSelectionStrategy selectionStrategy;
    private final PromptKnowledgeEnforcement enforcement;

    public RAGAgent(ChatLanguageModel model, Knowledge knowledge, KnowledgeSelectionStrategy selectionStrategy, @Nullable PromptKnowledgeEnforcement enforcement) {
        super(model);
        this.knowledge = knowledge;
        this.selectionStrategy = selectionStrategy;
        this.enforcement = enforcement == null ? PromptKnowledgeEnforcement.MEDIUM : enforcement;
    }

    public String getPrePrompt() {
        return "Given the following knowledge:";
    }

    public String getPostPrompt() {
        return PromptKnowledgeEnforcement.getKnowledgeEnforcementPostPrompt(this.enforcement);
    }

    @Override
    public String ask(final String prompt) {
        final var similar = this.knowledge.extract(prompt, this.selectionStrategy);
        final var msg = String.join("\n", this.getPrePrompt(), similar, this.getPostPrompt());
        return super.model.chat(UserMessage.from(msg)).aiMessage().text();
    }

    enum PromptKnowledgeEnforcement {
        LOW("also based on"), MEDIUM("MAINLY referring to"), HIGH("REFERRING ONLY to");

        private String text;

        PromptKnowledgeEnforcement(final String text) {
            this.text = text;
        }

        protected static String getKnowledgeEnforcementPostPrompt(final PromptKnowledgeEnforcement enforcement) {
            return "\nReply to the following question" + enforcement.text + " the provided knowledge.";
        }
    }
}


