package pl;

import pl.classification.ClassificationModel;
import pl.classification.KNNClassificator;
import pl.model.Article;
import pl.model.ArticleRepresentation;
import pl.model.TopicLabel;
import pl.reader.ArticleReader;
import pl.reader.VectorsReader;
import pl.representation.ArticleRepresentationService;
import pl.representation.ArticleRepresentationServiceImpl;
import pl.representation.ContentSanitizer;
import pl.representation.ContentSanitizerImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Prosze podać ścierzke do pliku z vectorami");
            System.exit(0);
        }
        final String vectorFileName = args[0];
        final String articleDirectory = args[1];
        final VectorsReader vectorsReader = new VectorsReader(Paths.get(vectorFileName));
        final Map<String, List<Double>> globalVectors = vectorsReader.getGlobalVectors();
        final ArticleReader articleReader = new ArticleReader(Paths.get(articleDirectory));
        final Article testArticle = articleReader.toArticle(new File("zdrowie_162.txt"),"HEALTH");
        final List<Article> articles = articleReader.getArticles();
        final ContentSanitizer contentSanitizer = new ContentSanitizerImpl();
        final ArticleRepresentationService articleRepresentationService = new ArticleRepresentationServiceImpl(globalVectors, contentSanitizer);
        final List<ArticleRepresentation> articleRepresentations = articleRepresentationService.crateRepresentation(articles);
        final ArticleRepresentation testRepresentation = articleRepresentationService.createRepresentation(testArticle);
        final ClassificationModel<ArticleRepresentation> model = new KNNClassificator().trainModel(articleRepresentations);
        //final Map<ArticleRepresentation, Integer> prediction = model.classify(articleRepresentations);
        printPrediction(testRepresentation, model.classify(testRepresentation));
//        prediction.forEach(App::printPrediction);
    }

    private static void printPrediction(ArticleRepresentation articleRepresentation, Integer label) {
        final TopicLabel topic = TopicLabel.valueOf(articleRepresentation.getLabel());
        final TopicLabel prediction = TopicLabel.valueOf(label);
        System.out.printf("Article tile: %s, article topic %s, prediction %s, correctness %b %n",
                articleRepresentation.getTitle(),
                topic,
                prediction,
                topic == prediction);
    }
}
