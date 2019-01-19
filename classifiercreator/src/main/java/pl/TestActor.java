package pl;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import pl.classification.article.ArticleRepresentationClassificatorFactory;
import pl.classification.ClassificationModel;
import pl.classification.actor.ClassificatorActor;
import pl.classification.actor.ModelMessage;
import pl.classification.actor.TrainMessage;
import pl.model.ArticleRepresentation;
import pl.performace_tests.ClassificationTestLogger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestActor extends AbstractActor {

    private ArticleRepresentationClassificatorFactory factory = new ArticleRepresentationClassificatorFactory();
    private final List<ArticleRepresentation> test;
    private final List<ArticleRepresentation> train;

    public TestActor(List<ArticleRepresentation> data) {
        Collections.shuffle(data);
        int size = data.size();
        test = data.subList(0, size / 10);
        train = data.subList(size / 10 + 1, size);
    }

    public static Props props(List<ArticleRepresentation> data){
        return Props.create(TestActor.class, ()-> new TestActor(data));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("TEST", this::test)
                .match(ModelMessage.class, this::testModel)
                .build();
    }

    private void testModel(ModelMessage<ArticleRepresentation> modelMessage) {
        ClassificationModel<ArticleRepresentation> model = modelMessage.model;
        Map<ArticleRepresentation, Integer> classify = model.classify(test);
        classify.forEach(ClassificationTestLogger::printPrediction);
    }

    private void test(String s) {
        Props props = ClassificatorActor.props(factory.KNNClassificator());
        ActorRef knn = getContext().actorOf(props, "KNN");
        knn.tell(TrainMessage.of(train), getSelf());
    }
}
