package pl;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import pl.classification.article.ArticleRepresentationClassificatorFactory;
import pl.model.Article;
import pl.performace_tests.actor.CrossValidationActor;
import pl.performace_tests.actor.message.CrossTestClassificatorMessage;
import pl.reader.actor.message.ArticlesMessage;
import pl.reader.actor.message.ReadArticleMessage;
import pl.reader.actor.message.ReadVectorMessage;
import pl.reader.actor.message.VectorsMessage;
import pl.representation.actor.message.CreateRepresentationMessage;
import pl.representation.actor.message.RepresentationMessage;

import java.util.List;
import java.util.Map;


public class SupervisorActor extends LoggerActor {
    public static Props props(ActorRef vectorReaderActor,
                              ActorRef articlesReaderActor,
                              ActorRef representationActor) {
        return Props.create(SupervisorActor.class, () -> new SupervisorActor(vectorReaderActor, articlesReaderActor, representationActor));
    }

    final private ActorRef vectorReaderActor;
    final private ActorRef articlesReaderActor;
    final private ActorRef representationActor;

    private final ArticleRepresentationClassificatorFactory factory = new ArticleRepresentationClassificatorFactory();

    private Map<String, List<Double>> vectors;
    private List<Article> articles;

    public SupervisorActor(ActorRef vectorReaderActor, ActorRef articlesReaderActor, ActorRef representationActor) {
        this.vectorReaderActor = vectorReaderActor;
        this.articlesReaderActor = articlesReaderActor;
        this.representationActor = representationActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("START", s -> start())
                .match(VectorsMessage.class, this::vectorsReceived)
                .match(ArticlesMessage.class, this::articlesReceived)
                .match(RepresentationMessage.class, this::representationReceived)
                .build();
    }

    private void start() {
        final String vectorFileName = Properties.stringValue("vectorsFile");
        final boolean useStopList = Properties.boolValue("useStopList");
        final int stopListSize = Properties.intValue("stopListSize");

        vectorReaderActor.tell(ReadVectorMessage.of(vectorFileName, useStopList, stopListSize), getSelf());

        final String articleDirectory = Properties.stringValue("articlesDirectory");
        articlesReaderActor.tell(ReadArticleMessage.of(articleDirectory), getSelf());
    }

    private void vectorsReceived(VectorsMessage message) {
        this.vectors = message.vectors;
        if (hasCompleteData()) createRepresentation();
    }

    private void articlesReceived(ArticlesMessage message) {
        this.articles = message.articles;
        if (hasCompleteData()) createRepresentation();
    }

    private boolean hasCompleteData() {
        return vectors != null && articles != null;
    }

    private void createRepresentation() {
        final boolean normalize = Properties.boolValue("normalize");
        representationActor.tell(CreateRepresentationMessage.of(articles, vectors, normalize), getSelf());
    }

    private void representationReceived(RepresentationMessage message) {
        final int crossValidationBatchesNo = Properties.intValue("crossValidationBatchesNo");

        ActorRef supervisor = getContext().actorOf(CrossValidationActor.props(factory.KNNClassificator(15)), "CrossValidator");
        supervisor.tell(CrossTestClassificatorMessage.of(message.representation, crossValidationBatchesNo), getSelf());
    }
}
