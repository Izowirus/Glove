package pl.classification;

import org.apache.commons.lang3.ArrayUtils;
import pl.model.ArticleRepresentation;
import smile.classification.Classifier;
import smile.classification.KNN;


class ArticleClassificationModel implements ClassificationModel<ArticleRepresentation> {

    private final Classifier<double[]> model;

    public ArticleClassificationModel(Classifier<double[]> model) {
        this.model = model;
    }

    @Override
    public int classify(ArticleRepresentation object) {
        final Double[] vector = object.getVector().toArray(new Double[0]);
        return model.predict(ArrayUtils.toPrimitive(vector));
    }
}
