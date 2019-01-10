package pl.representation;

import pl.model.Article;
import pl.model.ArticleRepresentation;

import java.util.Collection;
import java.util.List;

public interface ArticleRepresentationService {

    ArticleRepresentation createRepresentation(Article article);

    List<ArticleRepresentation> crateRepresentation(Collection<Article> articles);

}
