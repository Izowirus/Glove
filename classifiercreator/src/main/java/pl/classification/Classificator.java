package pl.classification;

import java.util.Collection;

public interface Classificator<T> {

    ClassificationModel<T> trainModel(Collection<? extends T> trainSet);
}
