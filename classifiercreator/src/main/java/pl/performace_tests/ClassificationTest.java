package pl.performace_tests;


import pl.classification.ClassificationModel;
import pl.model.ArticleRepresentation;
import pl.model.Category;

import java.util.Collection;
public class ClassificationTest {


    public ClassificationResults checkClassificationAccuracy(ClassificationModel<ArticleRepresentation> model,
                                                             Collection<ArticleRepresentation> testArticleRepresentations) {

        int categotiesNo = Category.values().length;
        int[] catArticlesNums = new int[categotiesNo];
        int[] catArticlesProperClassifiesNums = new int[categotiesNo];

        testArticleRepresentations.forEach(articleRepresentation -> {
            int properLabelValue = articleRepresentation.getLabel();
            catArticlesNums[properLabelValue] += 1;
            int prediction = model.classify(articleRepresentation);
            if (prediction == properLabelValue) {
                catArticlesProperClassifiesNums[properLabelValue] += 1;
            }
        });
        return new ClassificationResults(catArticlesNums, catArticlesProperClassifiesNums);
    }

}
