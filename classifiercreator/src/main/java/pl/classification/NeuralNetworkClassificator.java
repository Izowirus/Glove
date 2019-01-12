package pl.classification;

import pl.model.ArticleRepresentation;
import smile.classification.NeuralNetwork;


public class NeuralNetworkClassificator extends ArticleRepresentationClassificator {

    private NeuralNetwork.Trainer trainer;

    public NeuralNetworkClassificator(int epochs, int... neuronInLayers){
        trainer = new NeuralNetwork.Trainer(
                NeuralNetwork.ErrorFunction.CROSS_ENTROPY,
                NeuralNetwork.ActivationFunction.SOFTMAX,
                neuronInLayers)
                .setNumEpochs(epochs)
                .setLearningRate(0.5);
    }

    @Override
    protected ClassificationModel<ArticleRepresentation> train(double[][] vectors, int[] labels) {
        NeuralNetwork neuralNetwork = trainer.train(vectors, labels);
        return new ArticleClassificationModel(neuralNetwork);
    }
}
