package pl.performace_tests.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import lombok.AllArgsConstructor;
import pl.classification.Classificator;
import pl.classification.actor.ClassificatorActor;
import pl.classification.actor.ModelMessage;
import pl.classification.actor.TrainMessage;
import pl.model.ArticleRepresentation;
import pl.performace_tests.ClassificationResults;
import pl.performace_tests.ClassificationTest;
import pl.performace_tests.actor.message.TestClassificatorMessage;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class ValidatorActor extends AbstractActor {

    final private Classificator<ArticleRepresentation> classificator;
    final private ClassificationTest test = new ClassificationTest();

    public static Props props(Classificator<ArticleRepresentation> classificator){
      return Props.create(ValidatorActor.class, () -> new ValidatorActor(classificator));
    }

    @Override
    public void preStart() {
        System.out.println("ValidatorActor started");
    }

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                .match(TestClassificatorMessage.class, this::testClassificator)
                .build();
    }

    private void testClassificator(TestClassificatorMessage message) {
        try {
            Collection<ArticleRepresentation> train = message.train;
            ActorRef ref = getContext().actorOf(ClassificatorActor.props(classificator));
            CompletionStage<Object> ask = Patterns.ask(ref, TrainMessage.of(train), Duration.ofMinutes(1));
            ModelMessage<ArticleRepresentation>  model = (ModelMessage<ArticleRepresentation>) ask.toCompletableFuture().get();
            ClassificationResults classificationResults = test.checkClassificationAccuracy(model.model, message.test);
            getSender().tell(classificationResults, getSelf());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
