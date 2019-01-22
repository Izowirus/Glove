package pl.classification.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.AllArgsConstructor;
import pl.LoggerActor;
import pl.classification.ClassificationModel;
import pl.classification.Classificator;

@AllArgsConstructor
public class ClassificatorActor<T> extends LoggerActor {

    private Classificator<T> classificator;

    public static <T> Props props(Classificator<T> classificator){
        return Props.create(ClassificatorActor.class, () -> new ClassificatorActor<>(classificator));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TrainMessage.class, trainMessage -> {
                    ClassificationModel<T> classificationModel = classificator.trainModel(trainMessage.trainData);
                    getSender().tell(ModelMessage.of(classificationModel), getSelf());
                })
                .build();
    }
}
