package pl.performace_tests;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class ClassificationTest {

    private Classificator classificator;
    List<ArticleRepresentation> trainArticleRepresentations = new ArrayList<>();
    List<ArticleRepresentation> testArticleRepresentations =  new ArrayList<>();


    public ClassificationTest(Classificator classificator, ArticleRepresentationService representationCreator, List<Article> articles) {
        this.classificator = classificator;
        List<ArticleRepresentation>  articleRepresentations = representationCreator.crateRepresentation(articles);
        divideForTrainAndTest(articleRepresentations);
    }

    public void checkClassificationAccuracy() {

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
            printProgress(index + 1, allArticlesNo);
            printPrediction(articleRepresentation, prediction);
        });

        printClassicationResults(catArticlesNums, catArticlesProperClassifiesNums);
    }

    private void divideForTrainAndTest(List<ArticleRepresentation> articleRepresentations) {
        Arrays.asList(Category.values()).forEach(category ->{
            List<ArticleRepresentation> categoryArticleRepresentations = articleRepresentations.stream()
                    .filter(articleRepresentation -> category.getLabel() == articleRepresentation.getLabel())
                    .collect(Collectors.toList());
            int categoryArticlesNo = categoryArticleRepresentations.size();
            int middleIdx = categoryArticlesNo/2 + 1;
            trainArticleRepresentations.addAll(categoryArticleRepresentations.subList(0, middleIdx));
            testArticleRepresentations.addAll(categoryArticleRepresentations.subList(middleIdx, categoryArticlesNo));
        });
    }

    private static void printClassicationResults(int[] catArticlesNum, int[] catArticlesProperClassifiesNum) {
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

        int allGoodPredictions = IntStream.of(catArticlesProperClassifiesNum).sum();
        int allPredictions = IntStream.of(catArticlesNum).sum();
        float overallAccuracy = getAccuracy(allGoodPredictions, allPredictions);
        System.out.printf("Overall accuracy: %d/%d = %.2f%%",
                allGoodPredictions,
                allPredictions,
                overallAccuracy);
    }

    private static float getAccuracy(float allGoodPredictions, float allPredictions) {
        return allGoodPredictions / allPredictions * 100F;
    }

    private static void printPrediction(ArticleRepresentation articleRepresentation, Integer label) {
        final Category category = Category.valueOfInt(articleRepresentation.getLabel());
        final Category prediction = Category.valueOfInt(label);
        System.out.printf("Article tile: %s, article topic %s, prediction %s, correctness %b %n",
                articleRepresentation.getTitle(),
                category,
                prediction,
                category == prediction);
    }

    private static void printProgress(int currentNo, int allNo) {
        System.out.printf("%d/%d ", currentNo, allNo);
    }


}
