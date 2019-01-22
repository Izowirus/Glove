package pl.classification.article;

import pl.classification.ClassificationModel;
import pl.model.ArticleRepresentation;
import smile.classification.NeuralNetwork;


class NeuralNetworkClassificator extends ArticleRepresentationClassificator {

    private NeuralNetwork.Trainer trainer;

    public NeuralNetworkClassificator(int epochs, double eta, int... neuronInLayers){
        trainer = new NeuralNetwork.Trainer(
                NeuralNetwork.ErrorFunction.CROSS_ENTROPY,
                NeuralNetwork.ActivationFunction.SOFTMAX,
                neuronInLayers)
                .setNumEpochs(epochs)
                .setLearningRate(eta);
    }

    @Override
    protected ClassificationModel<ArticleRepresentation> train(double[][] vectors, int[] labels) {
        NeuralNetwork neuralNetwork = trainer.train(vectors, labels);
        return new ArticleClassificationModel(neuralNetwork);
    }
}
