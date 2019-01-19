package pl.edu.pw.elka.stud.wedtsag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    PapScraper papScraper;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        papScraper.scrape();
    }
}
