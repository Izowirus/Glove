package pl.edu.pw.elka.stud.wedtsag;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlArticle;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
@Slf4j
public class PapScraper {


    private static final String SITE_URI_WITH_PROTOCOL = "https://www.pap.pl";
    private static final String SITE_URI_WITH_PROTOCOL_AND_PAGE_NO_PATTERN = "https://www.pap.pl/%s?page=%d";
    private static final String[] CATEGORIES = {"kraj", "swiat", "gospodarka", "sport", "nauka", "kultura", "zdrowie", "ciekawostki"};
    private static final String ARTICLE_FILE_NAME_PATTERN = "%s_%d.txt";
    private URL homeUrl;
    private WebClient client;

    @Value("${outDirectoryPathname}")
    private String outDirPathname;

    private File outDir;


    public PapScraper() {
        try {
            homeUrl = new URL(SITE_URI_WITH_PROTOCOL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        outDir = Paths.get(outDirPathname).toFile();
    }

    public void scrape() {
        Arrays.asList(CATEGORIES).forEach(category -> {


            try {
                int articleCnt = 0;
                String categoryFirstPageAdress = new URL(homeUrl, category).toExternalForm();
                HtmlPage categoryPage = client.getPage(categoryFirstPageAdress);
                File categoryOutDirectory = Paths.get(outDir.getAbsolutePath(), category).toFile();
                FileUtils.forceMkdir(categoryOutDirectory);

                for (int pageIdx = 0; pageIdx <= getLastCategoryPageIdx(categoryPage); pageIdx++) {

                    String categoryCurrentPageAdress = String.format(SITE_URI_WITH_PROTOCOL_AND_PAGE_NO_PATTERN, category, pageIdx);
                    HtmlElement newsListElement = getNewsListElement(client.getPage(categoryCurrentPageAdress));
                    List<HtmlElement> articlesLinkOnPage = getLinksFromNewsList(newsListElement);

                    for (HtmlElement articleLink : articlesLinkOnPage) {
                        scrapeLinkedPage(articleLink, categoryOutDirectory, String.format(ARTICLE_FILE_NAME_PATTERN, category, articleCnt));
                        articleCnt += 1;
                    }
                    if (articleCnt > 1400)
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private int getLastCategoryPageIdx(HtmlPage categoryPage) {
        String lastPageRelUrl = ((HtmlElement) categoryPage.getFirstByXPath("//a[@rel='last']")).getAttribute("href");
        return Integer.parseInt(new Scanner(lastPageRelUrl).findInLine("\\d+"));
    }

    private List<HtmlElement> getLinksFromNewsList(HtmlElement newsListElement) {
        return newsListElement.getByXPath("//li[@class='news col-sm-6']");
    }

    private HtmlElement getNewsListElement(HtmlPage page) {
        return page.getFirstByXPath("//ul[@class='newsList']");
    }

    private void scrapeLinkedPage(HtmlElement articleLink, File outDir, String outFilename) {
        try {
            String linkRelPath = ((HtmlElement) articleLink.getFirstByXPath("div/a")).getAttribute("href");
            String articleUri = new URL(homeUrl, linkRelPath).toExternalForm();
            HtmlPage articlePage = client.getPage(articleUri);
            HtmlArticle article = articlePage.getFirstByXPath("//article[@role='article']");
            File outFile = Paths.get(outDir.getAbsolutePath(), outFilename).toFile();
            String articleContent = article.getTextContent().split("\\(PAP[.]*")[0].replaceAll(".*Fot.*", "").trim();

            try (FileWriter fileWriter = new FileWriter(outFile)) {
                fileWriter.write(articleContent);
                log.info(String.format("File %s created.", outFilename));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
