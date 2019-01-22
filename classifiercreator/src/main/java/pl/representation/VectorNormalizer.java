package pl.representation;

import pl.model.ArticleRepresentation;

import java.util.Collection;

public interface VectorNormalizer {
    void normalize(Collection<ArticleRepresentation> articles);
}
