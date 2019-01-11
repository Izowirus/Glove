package pl.reader;

import pl.model.Article;
import pl.model.Category;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleReader {

    private List<Article> articles = new ArrayList<>();

    public ArticleReader(Path articleDirectory) {
        readArticles(articleDirectory);
    }

    private void readArticles(Path articleDirectory) {
        final Collection<String> allowedTopics = Category.categoriesNames();
        final File articles = new File(articleDirectory.toUri());
        final List<File> categories = Arrays.stream(articles.listFiles())
                .filter(File::isDirectory)
                .filter(file -> allowedTopics.contains(file.getName()))
                .collect(Collectors.toList());
        categories.forEach(this::readCategory);
    }

    private void readCategory(File file) {
        final String topic = file.getName();
        Arrays.stream(file.listFiles())
                .map(f -> toArticle(f, topic))
                .forEach(articles::add);
    }

    public Article toArticle(File file, String topic) {
        try {
            final String content = Files.readAllLines(file.toPath()).stream()
                    .reduce("", String::concat);
            return new Article(topic, content, file.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Article> getArticles() {
        return articles;
    }
}
