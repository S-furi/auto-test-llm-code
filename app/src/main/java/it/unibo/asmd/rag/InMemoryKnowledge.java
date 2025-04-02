package it.unibo.asmd.rag;


import dev.langchain4j.model.embedding.EmbeddingModel;
import it.unibo.asmd.generator.utils.Pair;
import it.unibo.asmd.generator.utils.Vector;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryKnowledge implements Knowledge {
    private final EmbeddingModel model;
    private final Map<Vector, String> knowledge;

    public InMemoryKnowledge(final EmbeddingModel model, final String knowledge) {
        this.model = model;

        this.knowledge = Arrays.stream(knowledge.split("\n\n"))
                .map(data -> Pair.of(data, model.embed(data)))
                .map(pair -> pair.mapY(emb -> emb.content().vector()))
                .map(pair -> pair.mapY(Vector::fromFloatArray))
                .collect(Collectors.toMap(Pair::getY, Pair::getX));
    }

    @Override
    public String extract(final String text, final KnowledgeSelectionStrategy strategy) {
        final var req = Vector.fromFloatArray(model.embed(text).content().vector());
        return strategy.select(req, this.knowledge);
    }
}
