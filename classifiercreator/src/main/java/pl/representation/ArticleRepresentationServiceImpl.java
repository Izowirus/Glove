package pl.representation;

import lombok.Setter;
import pl.Properties;
import pl.model.Article;
import pl.model.ArticleRepresentation;
import pl.model.Category;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
public class ArticleRepresentationServiceImpl implements ArticleRepresentationService {

    private final Map<String, List<Double>> globalVectors;
    private final ContentFormatter contentFormatter;

    public ArticleRepresentationServiceImpl(Map<String, List<Double>> globalVectors,
                                            ContentFormatter contentFormatter) {
        this.globalVectors = globalVectors;
        this.contentFormatter = contentFormatter;
    }

    @Override
    public ArticleRepresentation createRepresentation(Article article) {
        final String text = contentFormatter.format(article.getContent());
        final Integer label = Category.valueOfString(article.getCategoryLabel()).getLabel();
        final String title = article.getTitle();
        final List<Double> vector = createVector(text);
        return new ArticleRepresentation(label, vector, title);
    }

    private List<Double> createVector(String text) {
        List<List<Double>> vectors = Arrays.stream(text.split("[ ]+"))
                .map(globalVectors::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        double size = (double) vectors.size();

        List<Double> vector = vectors.stream()
                .reduce(this::reduceVectors)
                .orElseGet(Collections::emptyList);

        if (Properties.boolValue("average")) {
            return vector.stream()
                    .map(d -> d / size)
                    .collect(Collectors.toList());
        } else {
            return vector;
        }

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
