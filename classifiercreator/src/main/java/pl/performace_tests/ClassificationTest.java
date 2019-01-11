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
import java.util.stream.IntStream;

@Slf4j
public class ClassificationTest {

    private Classificator classificator;
    List<ArticleRepresentation> articleRepresentations;


    public ClassificationTest(Classificator classificator, ArticleRepresentationService representationCreator, List<Article> articles) {
        this.classificator = classificator;
        this.articleRepresentations = representationCreator.crateRepresentation(articles);
    }

    public void checkClassificationAccuracy() {

        List<Category> allCategories = new ArrayList<Category>(Arrays.asList(Category.values()));
        ClassificationModel<ArticleRepresentation> model = classificator.trainModel(articleRepresentations);
        int categotiesNo = allCategories.size();
        int[] catArticlesNums = new int[categotiesNo];
        int[] catArticlesProperClassifiesNums = new int[categotiesNo];

        int allArticlesNo = articleRepresentations.size();

        IntStream.range(0, allArticlesNo).forEach(index -> {
            ArticleRepresentation articleRepresentation = articleRepresentations.get(index);
            int properLabelValue = articleRepresentations.get(index).getLabel();
            catArticlesNums[properLabelValue] += 1;
            int prediction = model.classify(articleRepresentation);
            if (prediction == properLabelValue) {
                catArticlesProperClassifiesNums[properLabelValue] += 1;
            }
            printProgress(index + 1, allArticlesNo);
            printPrediction(articleRepresentation, prediction);
        });

        printClassicationResults(catArticlesNums, catArticlesNums);
    }

    private static void printClassicationResults(int[] catArticlesNum, int[] catArticlesProperClassifiesNum) {
        List<String> categoriesNames = Category.categoriesNames();
        Arrays.asList(Category.values()).forEach(topicLabel -> {
            int catIdx = topicLabel.getIntValue();
            int allFromCategory = catArticlesNum[catIdx];
            int properlyClassified = catArticlesProperClassifiesNum[catIdx];
            float categoryAccuracy = properlyClassified / allFromCategory;
            String categoryAccuracyFormat = "Category %s result: %d/%d = %f %n";
            System.out.printf(categoryAccuracyFormat,
                    categoriesNames.get(catIdx),
                    properlyClassified,
                    allFromCategory,
                    categoryAccuracy);
        });
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
