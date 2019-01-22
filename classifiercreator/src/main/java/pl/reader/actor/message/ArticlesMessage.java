package pl.reader.actor.message;

import lombok.AllArgsConstructor;
import pl.model.Article;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class ArticlesMessage {
    public final List<Article> articles;

    public static ArticlesMessage of(List<Article> articles){
        return new ArticlesMessage(Collections.unmodifiableList(articles));
    }
}
