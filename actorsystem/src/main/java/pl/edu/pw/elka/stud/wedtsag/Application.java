package pl.edu.pw.elka.stud.wedtsag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Value("${vectorsFilePathname}")
    String vectorsFilePathname;

    @Value("${vocabFilePathname}")
    String vocabFilePathname;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        Path vectorsPath = Paths.get(vectorsFilePathname);
        Path vocabPath = Paths.get(vocabFilePathname);
        ArticleReprCreator articleReprCreator = new ArticleReprCreator(vectorsPath, vocabPath);
        articleReprCreator.getRepresentation(null);
    }
}
