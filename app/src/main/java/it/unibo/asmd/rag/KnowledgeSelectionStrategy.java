package it.unibo.asmd.rag;

import it.unibo.asmd.generator.utils.Vector;

import java.util.Map;

public interface KnowledgeSelectionStrategy {
    String select(Vector req, Map<Vector, String> knowledge);
}
