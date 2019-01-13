package pl.performace_tests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.classification.ClassificationModel;
import pl.classification.Classificator;
import pl.model.Article;
import pl.model.ArticleRepresentation;
import pl.model.Category;
import pl.representation.ArticleRepresentationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClassificationTest {

    private Classificator<ArticleRepresentation> classificator;
    List<ArticleRepresentation> articleRepresentations;
    private boolean verbose;

    public ClassificationTest(Classificator<ArticleRepresentation> classificator,
                              ArticleRepresentationService representationCreator,
                              List<Article> articles,
                              boolean verbose) {
        this.classificator = classificator;
        this.verbose = verbose;
        this.articleRepresentations = representationCreator.crateRepresentation(articles);
    }


    public ClassificationTest(Classificator<ArticleRepresentation> classificator,
                              List<ArticleRepresentation> articleRepresentations,
                              boolean verbose) {
        this.classificator = classificator;
        this.verbose = verbose;
        this.articleRepresentations = articleRepresentations;
    }

    public void crossValidationTest(int batchNo) {
        int categotiesNo = Category.values().length;
        int[] allCatArticlesNums = new int[categotiesNo];
        int[] allCatArticlesProperClassifiesNums = new int[categotiesNo];
        List<List<ArticleRepresentation>> batches = divideWholeSetForBatches(articleRepresentations, batchNo);
        IntStream.range(0, batchNo).forEach(idx -> {
            List<ArticleRepresentation> testArticleRepresentations = new ArrayList<>(batches.get(idx));
            List<ArticleRepresentation> trainArticleRepresentations = new ArrayList<>(articleRepresentations);
            trainArticleRepresentations.removeAll(testArticleRepresentations);

            ClassificationResults classificationResults = checkClassificationAccuracy(trainArticleRepresentations, testArticleRepresentations);
            int[] catArticlesNums = classificationResults.getCatArticlesNums();
            int[] catArticlesProperClassifiesNums = classificationResults.getCatArticlesProperClassifiesNums();
            ClassificationTestLogger.printBatchResults(catArticlesNums, catArticlesProperClassifiesNums);

            IntStream.range(0, categotiesNo).forEach(catIdx -> {
                allCatArticlesNums[catIdx] += catArticlesNums[catIdx];
                allCatArticlesProperClassifiesNums[catIdx] += catArticlesProperClassifiesNums[catIdx];
            });

        });
        ClassificationTestLogger.printCategoriesStats(allCatArticlesNums, allCatArticlesProperClassifiesNums);
        ClassificationTestLogger.printClassificationResults(allCatArticlesNums, allCatArticlesProperClassifiesNums);
    }


    private ClassificationResults checkClassificationAccuracy(List<ArticleRepresentation> trainArticleRepresentations, List<ArticleRepresentation> testArticleRepresentations) {

        List<Category> allCategories = new ArrayList<>(Arrays.asList(Category.values()));
        ClassificationModel<ArticleRepresentation> model = classificator.trainModel(trainArticleRepresentations);
        int categotiesNo = allCategories.size();
        int[] catArticlesNums = new int[categotiesNo];
        int[] catArticlesProperClassifiesNums = new int[categotiesNo];

        int allArticlesNo = testArticleRepresentations.size();

        IntStream.range(0, allArticlesNo).forEach(index -> {
            ArticleRepresentation articleRepresentation = testArticleRepresentations.get(index);
            int properLabelValue = testArticleRepresentations.get(index).getLabel();
            catArticlesNums[properLabelValue] += 1;
            int prediction = model.classify(articleRepresentation);
            if (prediction == properLabelValue) {
                catArticlesProperClassifiesNums[properLabelValue] += 1;
            }
            if (verbose) {
                ClassificationTestLogger.printProgress(index + 1, allArticlesNo);
                ClassificationTestLogger.printPrediction(articleRepresentation, prediction);
            }
        });
        return new ClassificationResults(catArticlesNums, catArticlesProperClassifiesNums);
    }

    private List<List<ArticleRepresentation>> divideWholeSetForBatches(List<ArticleRepresentation> articleRepresentations, int batchNo) {
        List<List<ArticleRepresentation>> batches = new ArrayList<>();

        IntStream.range(0, Category.values().length).forEach(idx ->
                batches.add(new ArrayList<>())
        );

        Arrays.asList(Category.values()).forEach(category -> {
            List<ArticleRepresentation> categoryArticleRepresentations = getCategoryArticlesRepresentations(articleRepresentations, category);
            List<List<ArticleRepresentation>> categoryBatches = divideForBatches(categoryArticleRepresentations, batchNo);

            IntStream.range(0, categoryBatches.size()).forEach(idx -> {
                List<ArticleRepresentation> batch = batches.get(idx);
                batch.addAll(categoryBatches.get(idx));
            });
        });
        return batches;
    }

    private List<ArticleRepresentation> getCategoryArticlesRepresentations(List<ArticleRepresentation> articleRepresentations, Category category) {
        return articleRepresentations.stream()
                .filter(articleRepresentation -> category.getLabel() == articleRepresentation.getLabel())
                .collect(Collectors.toList());
    }

    private List<List<ArticleRepresentation>> divideForBatches(List<ArticleRepresentation> articleRepresentations, int batchesNo) {
        List<List<ArticleRepresentation>> batches = new ArrayList<>();
        List<Integer> batchEdgeIdxs = linearlyDivideSpace(0, articleRepresentations.size(), batchesNo);
        IntStream.range(1, batchEdgeIdxs.size()).forEach(idx ->
                batches.add(articleRepresentations.subList(batchEdgeIdxs.get(idx - 1), batchEdgeIdxs.get(idx))));
        return batches;
    }

    private static List<Integer> linearlyDivideSpace(int startInclusive, int endInclusive, int segmentNo) {
        List<Integer> points = new ArrayList<>();
        int segmentSize = (endInclusive - startInclusive) / segmentNo;
        for (int point = startInclusive; point <= endInclusive - segmentSize; point += segmentSize) {
            points.add(point);
        }
        points.add(endInclusive);
        return points;
    }

    @AllArgsConstructor
    @Getter
    class ClassificationResults {
        int[] catArticlesNums;
        int[] catArticlesProperClassifiesNums;
    }


}
