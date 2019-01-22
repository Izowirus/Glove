package pl.performace_tests;

import lombok.NoArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import pl.model.ArticleRepresentation;
import pl.model.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor
public class ArticleSplitter {

    public List<List<ArticleRepresentation>> divideWholeSetForBatches(Collection<ArticleRepresentation> articleRepresentations, int batchNo) {
        List<List<ArticleRepresentation>> batches = new ArrayList<>();

        IntStream.range(0, batchNo).forEach(idx ->
                batches.add(new ArrayList<>())
        );

        Arrays.stream(Category.values())
            .map(category -> getCategoryArticlesRepresentations(articleRepresentations, category))
            .map(articles -> ListUtils.partition(articles, articles.size()/batchNo))
            .forEach(categoryBatches ->
                IntStream.range(0, batchNo).forEach(idx -> {
                    List<ArticleRepresentation> batch = batches.get(idx);
                    batch.addAll(categoryBatches.get(idx));
            })
        );
        return batches;
    }

    private List<ArticleRepresentation> getCategoryArticlesRepresentations(Collection<ArticleRepresentation> articleRepresentations, Category category) {
        return articleRepresentations.stream()
                .filter(articleRepresentation -> category.getLabel() == articleRepresentation.getLabel())
                .collect(Collectors.toList());
    }
}
