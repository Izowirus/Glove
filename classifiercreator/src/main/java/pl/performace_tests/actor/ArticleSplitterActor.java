package pl.performace_tests.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.AskableActorRef;
import pl.model.ArticleRepresentation;
import pl.performace_tests.ArticleSplitter;
import pl.performace_tests.actor.message.SplitRequestMessage;
import pl.performace_tests.actor.message.SplitResponseMessage;

import java.util.List;

public class ArticleSplitterActor extends AbstractActor {

    private ArticleSplitter articleSplitter = new ArticleSplitter();

    public static Props props(){
        return Props.create(ArticleSplitterActor.class, ArticleSplitterActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SplitRequestMessage.class, this::splitData)
                .build();
    }

    @Override
    public void preStart() {
        System.out.println("ArticleSplitterActor started");
    }

    private void splitData(SplitRequestMessage requestMessage) {
        List<List<ArticleRepresentation>> splitedData = articleSplitter.divideWholeSetForBatches(requestMessage.data, requestMessage.batchNo);
        getSender().tell(SplitResponseMessage.of(splitedData, requestMessage.data), getSelf());
    }
}
