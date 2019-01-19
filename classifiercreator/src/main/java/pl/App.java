package pl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import pl.classification.Classificator;
import pl.classification.article.ArticleRepresentationClassificatorFactory;
import pl.model.Article;
import pl.model.ArticleRepresentation;
import pl.performace_tests.ClassificationTest;
import pl.performace_tests.actor.CrossValidationActor;
import pl.performace_tests.actor.message.CrossTestClassificatorMessage;
import pl.reader.ArticleReader;
import pl.reader.VectorsReader;
import pl.representation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) throws IOException {

        final String vectorFileName = Properties.stringValue("vectorsFile");
        final String articleDirectory = Properties.stringValue("articlesDirectory");
        final boolean useStopList = Properties.boolValue("useStopList");
        final boolean normalize = Properties.boolValue("normalize");
        final int stopListSize = Properties.intValue("stopListSize");
        final int crossValidationBatchesNo = Properties.intValue("crossValidationBatchesNo");

        final VectorsReader vectorsReader;
        if (useStopList) {
            vectorsReader = new VectorsReader(Paths.get(vectorFileName), stopListSize);
        } else {
            vectorsReader = new VectorsReader(Paths.get(vectorFileName));
        }
        final Map<String, List<Double>> globalVectors = vectorsReader.getGlobalVectors();

        System.out.println("Vectors read");

        final ArticleReader articleReader = new ArticleReader(Paths.get(articleDirectory));
        final List<Article> articles = articleReader.getArticles();

        final ContentFormatter contentFormatter = new ContentFormatterImpl();
        final ArticleRepresentationService articleRepresentationService = new ArticleRepresentationServiceImpl(globalVectors, contentFormatter);
        final List<ArticleRepresentation> articleRepresentations = articleRepresentationService.crateRepresentation(articles);
        if (normalize) {
            new VectorNormalizer().minMaxNormalize(articleRepresentations);
        }
        final ArticleRepresentationClassificatorFactory factory = new ArticleRepresentationClassificatorFactory();

        ActorSystem test = ActorSystem.create("TEST");

        try {
            ActorRef supervisor = test.actorOf(CrossValidationActor.props(factory.KNNClassificator(15)), "CrossValidator");
            supervisor.tell(CrossTestClassificatorMessage.of(articleRepresentations), ActorRef.noSender());


            System.out.println("Press ENTER to exit the system");
            System.in.read();
        } finally {
            test.terminate();
        }


    }

}
