package pl.representation;

import pl.model.ArticleRepresentation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinMaxNormalizer implements VectorNormalizer {

    private double[] min;
    private double[] max;

    @Override
    public void normalize(Collection<ArticleRepresentation> articles){
        int size = articles.stream().findAny().map(ArticleRepresentation::getVector).map(List::size).orElse(0);
        min = new double[size];
        max = new double[size];

        IntStream.range(0,size).forEach(i ->{
            List<Double> values = articles.stream()
                    .map(ArticleRepresentation::getVector)
                    .map(vector -> vector.get(i))
                    .collect(Collectors.toList());
            min[i] = Collections.min(values);
            max[i] = Collections.max(values);
        });

        articles.stream()
                .map(ArticleRepresentation::getVector)
                .forEach(this::minMaxNormalize);
    }

    private void minMaxNormalize(List<Double> doubles) {
        for(int i = 0; i < doubles.size(); i++){
            double value = doubles.get(i);
            doubles.set(i, (value - min[i])/(max[i] - min[i]));
        }
    }
}
