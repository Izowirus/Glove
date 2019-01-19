package pl.classification.article;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.classification.ClassificationModel;
import pl.model.ArticleRepresentation;
import smile.classification.KNN;


@AllArgsConstructor
@NoArgsConstructor
class KNNClassificator extends ArticleRepresentationClassificator {

    int k = 1;

    @Override
    protected ClassificationModel<ArticleRepresentation> train(double[][] vectors, int[] labels) {
        final KNN<double[]> model = KNN.learn(vectors, labels, k);

        return new ArticleClassificationModel(model);
    }

}
