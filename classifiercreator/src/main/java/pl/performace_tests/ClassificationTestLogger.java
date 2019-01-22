package pl.performace_tests;

import pl.model.ArticleRepresentation;
import pl.model.Category;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ClassificationTestLogger {

    public static void printBatchResults(ClassificationResults results, int batchId) {

        int allGoodPredictions = IntStream.of(results.catArticlesProperClassifiesNums).sum();
        int allPredictions = IntStream.of(results.catArticlesNums).sum();
        float overallAccuracy = getAccuracy(allGoodPredictions, allPredictions);
        System.out.printf("Batch Id: %d - accuracy: %d/%d = %.2f%% %n",
                batchId,
                allGoodPredictions,
                allPredictions,
                overallAccuracy);
    }

    public static void printAgregatedResults(ClassificationResults results, int agregatedBatches, int allBatches) {

        int allGoodPredictions = IntStream.of(results.catArticlesProperClassifiesNums).sum();
        int allPredictions = IntStream.of(results.catArticlesNums).sum();
        float overallAccuracy = getAccuracy(allGoodPredictions, allPredictions);
        System.out.printf("Batches %d/%d agregated accuracy: %d/%d = %.2f%% %n%n",
                agregatedBatches,
                allBatches,
                allGoodPredictions,
                allPredictions,
                overallAccuracy);
    }

    public static void printClassificationResults(ClassificationResults results) {

        int allGoodPredictions = IntStream.of(results.catArticlesProperClassifiesNums).sum();
        int allPredictions = IntStream.of(results.catArticlesNums).sum();
        float overallAccuracy = getAccuracy(allGoodPredictions, allPredictions);
        System.out.printf("Overall accuracy: %d/%d = %.2f%% %n%n",
                allGoodPredictions,
                allPredictions,
                overallAccuracy);
    }

    public static void printCategoriesStats(ClassificationResults results) {
        List<String> categoriesNames = Category.categoriesNames();
        Arrays.asList(Category.values()).forEach(topicLabel -> {
            int catIdx = topicLabel.getLabel();
            int allFromCategory = results.catArticlesNums[catIdx];
            int properlyClassified = results.catArticlesProperClassifiesNums[catIdx];
            float categoryAccuracy = getAccuracy(properlyClassified, allFromCategory);
            String categoryAccuracyFormat = "Category %s result: %d/%d = %.2f%% %n";
            System.out.printf(categoryAccuracyFormat,
                    categoriesNames.get(catIdx),
                    properlyClassified,
                    allFromCategory,
                    categoryAccuracy);
        });
    }

    public static void printPrediction(ArticleRepresentation articleRepresentation, Integer label) {
        final Category category = Category.valueOfInt(articleRepresentation.getLabel());
        final Category prediction = Category.valueOfInt(label);
        System.out.println("Categories stats:");
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
