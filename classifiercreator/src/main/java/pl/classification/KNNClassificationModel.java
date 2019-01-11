package pl.classification;

import org.apache.commons.lang3.ArrayUtils;
import pl.model.ArticleRepresentation;
import smile.classification.KNN;


public class KNNClassificationModel implements ClassificationModel<ArticleRepresentation> {

    private final KNN<double[]> model;

    public KNNClassificationModel(KNN<double[]> model) {
        this.model = model;
    }

    @Override
    public int classify(ArticleRepresentation object) {
        final Double[] vector = object.getVector().toArray(new Double[0]);
        return model.predict(ArrayUtils.toPrimitive(vector));
    }
}
