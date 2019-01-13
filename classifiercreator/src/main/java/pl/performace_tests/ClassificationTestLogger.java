package pl.performace_tests;

import pl.model.ArticleRepresentation;
import pl.model.Category;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ClassificationTestLogger {

    static void printBatchResults(int[] catArticlesNum, int[] catArticlesProperClassifiesNum) {

        int allGoodPredictions = IntStream.of(catArticlesProperClassifiesNum).sum();
        int allPredictions = IntStream.of(catArticlesNum).sum();
        float overallAccuracy = getAccuracy(allGoodPredictions, allPredictions);
        System.out.printf("Batch accuracy: %d/%d = %.2f%% %n%n",
                allGoodPredictions,
                allPredictions,
                overallAccuracy);
    }

    static void printClassificationResults(int[] catArticlesNum, int[] catArticlesProperClassifiesNum) {

        int allGoodPredictions = IntStream.of(catArticlesProperClassifiesNum).sum();
        int allPredictions = IntStream.of(catArticlesNum).sum();
        float overallAccuracy = getAccuracy(allGoodPredictions, allPredictions);
        System.out.printf("Overall accuracy: %d/%d = %.2f%% %n%n",
                allGoodPredictions,
                allPredictions,
                overallAccuracy);
    }

    static void printCategoriesStats(int[] catArticlesNum, int[] catArticlesProperClassifiesNum) {
        List<String> categoriesNames = Category.categoriesNames();
        Arrays.asList(Category.values()).forEach(topicLabel -> {
            int catIdx = topicLabel.getLabel();
            int allFromCategory = catArticlesNum[catIdx];
            int properlyClassified = catArticlesProperClassifiesNum[catIdx];
            float categoryAccuracy = getAccuracy(properlyClassified, allFromCategory);
            String categoryAccuracyFormat = "Category %s result: %d/%d = %.2f%% %n";
            System.out.printf(categoryAccuracyFormat,
                    categoriesNames.get(catIdx),
                    properlyClassified,
                    allFromCategory,
                    categoryAccuracy);
        });
    }

    static void printPrediction(ArticleRepresentation articleRepresentation, Integer label) {
        final Category category = Category.valueOfInt(articleRepresentation.getLabel());
        final Category prediction = Category.valueOfInt(label);
        System.out.printf("Article tile: %s, article topic %s, prediction %s, correctness %b %n",
                articleRepresentation.getTitle(),
                category,
                prediction,
                category == prediction);
    }

    static void printProgress(int currentNo, int allNo) {
        System.out.printf("%d/%d ", currentNo, allNo);
    }

    private static float getAccuracy(float allGoodPredictions, float allPredictions) {
        return allGoodPredictions / allPredictions * 100F;
    }
}
