package pl;

import pl.classification.*;
import pl.model.Article;
import pl.model.ArticleRepresentation;
import pl.performace_tests.ClassificationTest;
import pl.reader.ArticleReader;
import pl.reader.VectorsReader;
import pl.representation.ArticleRepresentationService;
import pl.representation.ArticleRepresentationServiceImpl;
import pl.representation.ContentFormatter;
import pl.representation.ContentFormatterImpl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Prosze podać ścieżkę do pliku z vectorami");
            System.exit(0);
        }
        final String vectorFileName = args[0];
        final String articleDirectory = args[1];

        final VectorsReader vectorsReader = new VectorsReader(Paths.get(vectorFileName));
        final Map<String, List<Double>> globalVectors = vectorsReader.getGlobalVectors();

        final ArticleReader articleReader = new ArticleReader(Paths.get(articleDirectory));
        final List<Article> articles = articleReader.getArticles();

        final ContentFormatter contentFormatter = new ContentFormatterImpl();
        final ArticleRepresentationService articleRepresentationService = new ArticleRepresentationServiceImpl(globalVectors, contentFormatter);

        Stream.<Classificator<ArticleRepresentation>>of(
                new KNNClassificator(),
                new KNNClassificator(3),
                new KNNClassificator(5),
                new KNNClassificator(11),
                new KNNClassificator(21),
                new AdaBoostClassificator(),
                new SVMClassificator(1, 10),
                new SVMClassificator(1, 20),
                new SVMClassificator(1, 30),
                new SVMClassificator(1, 50),
                new SVMClassificator(0.5, 20),
                new SVMClassificator(0.5, 10),
                new SVMClassificator(0.5, 5),
                new NeuralNetworkClassificator(300, 50, 50, 8),
                new NeuralNetworkClassificator(300, 50, 10, 8),
                new NeuralNetworkClassificator(300, 50, 100, 8),
                new NeuralNetworkClassificator(600, 50, 200, 8),
                new NeuralNetworkClassificator(300, 50, 100, 50, 8),
                new NeuralNetworkClassificator(300, 50, 10, 10, 8),
                new NeuralNetworkClassificator(300, 50, 50, 10, 8)
        ).peek(System.out::println)
                .map(classificator -> new ClassificationTest(classificator, articleRepresentationService, articles, false))
                .forEach(ClassificationTest::checkClassificationAccuracy);
    }
}
