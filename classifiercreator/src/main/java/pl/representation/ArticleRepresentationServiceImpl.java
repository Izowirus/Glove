package pl.representation;

import pl.model.Article;
import pl.model.ArticleRepresentation;
import pl.model.TopicLabel;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArticleRepresentationServiceImpl implements ArticleRepresentationService {

    private final Map<String, List<Double>> globalVectors;
    private final ContentSanitizer contentSanitizer;

    public ArticleRepresentationServiceImpl(Map<String, List<Double>> globalVectors,
                                            ContentSanitizer contentSanitizer) {
        this.globalVectors = globalVectors;
        this.contentSanitizer = contentSanitizer;
    }

    @Override
    public ArticleRepresentation createRepresentation(Article article) {
        final String text = contentSanitizer.sanitize(article.getContent());
        final Integer label = TopicLabel.valueOf(article.getTopic()).getLabel();
        final String title = article.getTitle();
        final List<Double> vector = createVector(text);
        return new ArticleRepresentation(label, vector, title);
    }

    private List<Double> createVector(String text) {
        return Arrays.stream(text.split("[ ]+"))
                .map(globalVectors::get)
                .filter(Objects::nonNull)
                .reduce(this::reduceVectors)
                .orElseGet(Collections::emptyList);
    }

    private List<Double> reduceVectors(List<Double> a, List<Double> b) {
        return IntStream.range(0, a.size())
                .mapToObj(i -> Double.sum(a.get(i), b.get(i)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleRepresentation> crateRepresentation(Collection<Article> articles) {
        return articles.stream()
                .map(this::createRepresentation)
                .collect(Collectors.toList());
    }
}
