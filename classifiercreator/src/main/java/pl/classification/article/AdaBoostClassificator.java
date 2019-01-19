package pl.classification.article;

import pl.classification.ClassificationModel;
import pl.model.ArticleRepresentation;
import smile.classification.AdaBoost;

class AdaBoostClassificator extends  ArticleRepresentationClassificator {

    private AdaBoost.Trainer trainer = new AdaBoost.Trainer();

    @Override
    protected ClassificationModel<ArticleRepresentation> train(double[][] vectors, int[] labels) {
        return new ArticleClassificationModel(trainer.train(vectors, labels));
    }
}
