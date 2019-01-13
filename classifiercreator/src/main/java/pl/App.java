package pl;

import pl.classification.Classificator;
import pl.classification.KNNClassificator;
import pl.classification.NeuralNetworkClassificator;
import pl.classification.SVMClassificator;
import pl.model.Article;
import pl.model.ArticleRepresentation;
import pl.performace_tests.ClassificationTest;
import pl.reader.ArticleReader;
import pl.reader.VectorsReader;
import pl.representation.ArticleRepresentationService;
import pl.representation.ArticleRepresentationServiceImpl;
import pl.representation.ContentFormatter;
import pl.representation.ContentFormatterImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Prosze podać ścieżkę do pliku z vectorami");
            System.exit(0);
        }

        Properties properties = new Properties();
        FileInputStream propertiesInputStream = new FileInputStream("properties.txt");
        properties.load(propertiesInputStream);

        final String vectorFileName = properties.getProperty("vectorsFile");
        final String articleDirectory = properties.getProperty("articlesDirectory");
        final Boolean useStopList = Boolean.valueOf(properties.getProperty("useStopList"));
        final Integer stopListSize = Integer.valueOf(properties.getProperty("stopListSize"));

        final VectorsReader vectorsReader;
        if(useStopList) {
            vectorsReader = new VectorsReader(Paths.get(vectorFileName), stopListSize);
        }
        else {
            vectorsReader = new VectorsReader(Paths.get(vectorFileName));
        }
        final Map<String, List<Double>> globalVectors = vectorsReader.getGlobalVectors();

        System.out.println("Vectors read");

        final ArticleReader articleReader = new ArticleReader(Paths.get(articleDirectory));
        final List<Article> articles = articleReader.getArticles();

        final ContentFormatter contentFormatter = new ContentFormatterImpl();
        final ArticleRepresentationService articleRepresentationService = new ArticleRepresentationServiceImpl(globalVectors, contentFormatter);
        final List<ArticleRepresentation> articleRepresentations = articleRepresentationService.crateRepresentation(articles);

        Stream.<Classificator<ArticleRepresentation>>of(
                new KNNClassificator(15),
                new KNNClassificator(21),
                new KNNClassificator(25),
                new SVMClassificator(1, 30),
                new SVMClassificator(1, 50),
                new SVMClassificator(0.5, 20),
                new SVMClassificator(0.5, 10),
                new SVMClassificator(0.5, 5),
                new NeuralNetworkClassificator(300, 50, 50, 8),
                new NeuralNetworkClassificator(300, 50, 65, 8),
                new NeuralNetworkClassificator(300, 50, 80, 8)
        ).peek(System.out::println)
                .map(classificator -> new ClassificationTest(classificator, articleRepresentations, false))
                .forEach(ClassificationTest::checkClassificationAccuracy);
    }
}
