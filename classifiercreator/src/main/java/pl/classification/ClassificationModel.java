package pl.classification;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ClassificationModel<T> extends Serializable {

    int  classify(T object);

    default Map<T, Integer> classify(Collection<? extends T> objects){
        return objects.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        this::classify
                ));
    }
}
