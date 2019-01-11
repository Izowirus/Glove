package pl;

import pl.classification.KNNClassificator;
import pl.model.Article;
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

public class App {

    public static void main(String[] args) throws IOException {
        if(args.length == 0){
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

        new ClassificationTest(new KNNClassificator(), articleRepresentationService, articles).checkClassificationAccuracy();

    }


}
