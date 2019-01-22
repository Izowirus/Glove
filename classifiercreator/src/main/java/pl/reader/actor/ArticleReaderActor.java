package pl.reader.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import pl.LoggerActor;
import pl.model.Article;
import pl.reader.ArticleReader;
import pl.reader.actor.message.ArticlesMessage;
import pl.reader.actor.message.ReadArticleMessage;

import java.nio.file.Paths;
import java.util.List;

public class ArticleReaderActor extends LoggerActor {

    public static Props props(){
        return Props.create(ArticleReaderActor.class, ArticleReaderActor::new);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(ReadArticleMessage.class, this::readArticle)
                .build();
    }

    private void readArticle(ReadArticleMessage message) {
        final ArticleReader articleReader = new ArticleReader(Paths.get(message.fileName));
        final List<Article> articles = articleReader.getArticles();
        getSender().tell(ArticlesMessage.of(articles), getSelf());
    }
}
