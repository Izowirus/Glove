package pl.classification;

import pl.model.ArticleRepresentation;
import smile.classification.KNN;


public class KNNClassificator extends ArticleRepresentationClassificator {

    int k = 1;

    public KNNClassificator(int k) {
        this.k = k;
    }

    public KNNClassificator() {

    }

    @Override
    protected ClassificationModel<ArticleRepresentation> train(double[][] vectors, int[] labels) {
        final KNN<double[]> model = KNN.learn(vectors, labels, k);

        return new ArticleClassificationModel(model);
    }

}
