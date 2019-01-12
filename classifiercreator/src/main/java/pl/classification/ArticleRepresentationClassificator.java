package pl.classification;

import org.apache.commons.lang3.ArrayUtils;
import pl.model.ArticleRepresentation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

abstract class ArticleRepresentationClassificator implements Classificator<ArticleRepresentation> {

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

        return train(vectors, labels);
    }

    protected abstract ClassificationModel<ArticleRepresentation> train(double[][] vectors, int[] labels);
    
    @Override
    public String toString(){
        String className = this.getClass().getName();
        return className.substring(0,className.indexOf("Classificator"));
    }

}
