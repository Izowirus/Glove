package pl.classification.article;

import pl.classification.ClassificationModel;
import pl.model.ArticleRepresentation;
import smile.classification.SVM;
import smile.math.kernel.GaussianKernel;

class SVMClassificator extends  ArticleRepresentationClassificator {

    private SVM.Trainer<double[]> trainer;

    public SVMClassificator(double sigma, double C) {
        trainer = new SVM.Trainer<>(new GaussianKernel(sigma), C, 8, SVM.Multiclass.ONE_VS_ONE).setNumEpochs(30);
    }

    @Override
    protected ClassificationModel<ArticleRepresentation> train(double[][] vectors, int[] labels) {
        return new ArticleClassificationModel(trainer.train(vectors, labels));
    }
}
