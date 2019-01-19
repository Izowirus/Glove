package pl.classification.article;

import org.apache.commons.lang3.ArrayUtils;
import pl.classification.ClassificationModel;
import pl.model.ArticleRepresentation;
import smile.classification.Classifier;


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
