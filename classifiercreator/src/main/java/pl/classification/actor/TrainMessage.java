package pl.classification.actor;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class TrainMessage<T> {

    public static <T> TrainMessage<T>  of(Collection<T> data){
        return new TrainMessage<>(Collections.unmodifiableCollection(data));
    }

    final Collection<? extends T> trainData;
}
