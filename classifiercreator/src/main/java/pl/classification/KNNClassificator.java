package pl.classification;

import org.apache.commons.lang3.ArrayUtils;
import pl.model.ArticleRepresentation;
import smile.classification.KNN;
import smile.classification.NeuralNetwork;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class KNNClassificator implements Classificator<ArticleRepresentation> {

    @Override
    public ClassificationModel<ArticleRepresentation> trainModel(Collection<? extends ArticleRepresentation> trainSet) {
        final int[] labels = trainSet.stream()
                .mapToInt(ArticleRepresentation::getLabel)
                .toArray();

        final List<List<Double>> vectorsList = trainSet.stream()
                .map(ArticleRepresentation::getVector)
                .collect(Collectors.toList());

        final double[][] vectors = new double[vectorsList.size()][];

        for(int i = 0; i < vectorsList.size(); i ++){
            vectors[i] = ArrayUtils.toPrimitive(vectorsList.get(i).toArray(new Double[0]));
        }

//        System.out.println(labels.length);
//        System.out.println(vectors.length);
//        for(double[] test: vectors) System.out.println(test.length);


        final KNN<double[]> model = KNN.learn(vectors, labels);

        return new KNNClassificationModel(model);
    }
}
