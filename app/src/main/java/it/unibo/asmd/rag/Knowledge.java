package it.unibo.asmd.rag;

public interface Knowledge {
    String extract(String text, KnowledgeSelectionStrategy strategy);
}
