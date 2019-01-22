package pl.representation;

import pl.model.Article;
import pl.model.ArticleRepresentation;

import java.util.*;

public interface ArticleRepresentationService {

    ArticleRepresentation createRepresentation(Article article, Map<String, List<Double>> globalVectors);

    List<ArticleRepresentation> createRepresentation(Collection<Article> articles,  Map<String, List<Double>> globalVectors);

}
