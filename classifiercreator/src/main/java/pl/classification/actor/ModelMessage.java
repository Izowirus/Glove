package pl.classification.actor;

import lombok.AllArgsConstructor;
import pl.classification.ClassificationModel;

@AllArgsConstructor
public class ModelMessage<T> {
   public  final ClassificationModel<T>  model;

    public static <T> ModelMessage<T> of(ClassificationModel<T> model){
        return new ModelMessage<>(model);
    }
}
