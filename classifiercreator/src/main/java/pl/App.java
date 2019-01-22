package pl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import pl.reader.actor.ArticleReaderActor;
import pl.reader.actor.VectorsReaderActor;
import pl.representation.*;
import pl.representation.actor.ArticleRepresentationActor;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {

        final ContentFormatter contentFormatter = new ContentFormatterImpl();
        final ArticleRepresentationService articleRepresentationService = new ArticleRepresentationServiceImpl(contentFormatter);
        final MinMaxNormalizer minMaxNormalizer = new MinMaxNormalizer();

        ActorSystem system = ActorSystem.create("GloveClassificatorTester");

        final ActorRef vectorReaderActor = system.actorOf(VectorsReaderActor.props(), "VectorReaderActor");
        final ActorRef articleReaderActor = system.actorOf(ArticleReaderActor.props(), "ArticleReaderActor");
        final ActorRef articleRepresentationActor = system.actorOf(ArticleRepresentationActor.props(articleRepresentationService, minMaxNormalizer));

        final ActorRef supervisor = system.actorOf(SupervisorActor.props(vectorReaderActor, articleReaderActor, articleRepresentationActor), "SupervisorActor");


        try {
            supervisor.tell("START", ActorRef.noSender());


            System.out.println("Press ENTER to exit the system");
            System.in.read();
        } finally {
            system.terminate();
        }


    }

}
