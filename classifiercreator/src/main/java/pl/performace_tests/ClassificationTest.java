package pl.performace_tests;

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
    List<ArticleRepresentation> trainArticleRepresentations = new ArrayList<>();
    List<ArticleRepresentation> testArticleRepresentations =  new ArrayList<>();
    private boolean verbose;

    public ClassificationTest(Classificator<ArticleRepresentation> classificator,
                              ArticleRepresentationService representationCreator,
                              List<Article> articles,
                              boolean verbose) {
        this.classificator = classificator;
        this.verbose = verbose;
        List<ArticleRepresentation>  articleRepresentations = representationCreator.crateRepresentation(articles);
        divideForTrainAndTest(articleRepresentations);
    }


    public ClassificationTest(Classificator<ArticleRepresentation> classificator,
                              List<ArticleRepresentation>  articleRepresentations,
                              boolean verbose) {
        this.classificator = classificator;
        this.verbose = verbose;
        divideForTrainAndTest(articleRepresentations);
    }

    public ClassificationTest(Classificator<ArticleRepresentation> classificator,
                              ArticleRepresentationService representationCreator,
                              List<Article> articles) {
        this(classificator, representationCreator, articles, true);
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
            if(verbose) {
                printProgress(index + 1, allArticlesNo);
                printPrediction(articleRepresentation, prediction);
            }
        });

        printClassicationResults(catArticlesNums, catArticlesProperClassifiesNums);
    }

    private void divideForTrainAndTest(List<ArticleRepresentation> articleRepresentations) {
        Arrays.asList(Category.values()).forEach(category ->{
            List<ArticleRepresentation> categoryArticleRepresentations = articleRepresentations.stream()
                    .filter(articleRepresentation -> category.getLabel() == articleRepresentation.getLabel())
                    .collect(Collectors.toList());
            int categoryArticlesNo = categoryArticleRepresentations.size();
            int middleIdx = 3*categoryArticlesNo/4 + 1;
            trainArticleRepresentations.addAll(categoryArticleRepresentations.subList(0, middleIdx));
            testArticleRepresentations.addAll(categoryArticleRepresentations.subList(middleIdx, categoryArticlesNo));
        });
    }

    private void printClassicationResults(int[] catArticlesNum, int[] catArticlesProperClassifiesNum) {
        if(verbose) {
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

        int allGoodPredictions = IntStream.of(catArticlesProperClassifiesNum).sum();
        int allPredictions = IntStream.of(catArticlesNum).sum();
        float overallAccuracy = getAccuracy(allGoodPredictions, allPredictions);
        System.out.printf("Overall accuracy: %d/%d = %.2f%% %n%n",
                allGoodPredictions,
                allPredictions,
                overallAccuracy);
    }

    private float getAccuracy(float allGoodPredictions, float allPredictions) {
        return allGoodPredictions / allPredictions * 100F;
    }

    private void printPrediction(ArticleRepresentation articleRepresentation, Integer label) {
        final Category category = Category.valueOfInt(articleRepresentation.getLabel());
        final Category prediction = Category.valueOfInt(label);
        System.out.printf("Article tile: %s, article topic %s, prediction %s, correctness %b %n",
                articleRepresentation.getTitle(),
                category,
                prediction,
                category == prediction);
    }

    private void printProgress(int currentNo, int allNo) {
        System.out.printf("%d/%d ", currentNo, allNo);
    }


}
