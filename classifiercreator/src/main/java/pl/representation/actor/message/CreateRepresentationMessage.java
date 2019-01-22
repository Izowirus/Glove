package pl.representation.actor.message;

import lombok.AllArgsConstructor;
import pl.model.Article;

import java.util.*;

@AllArgsConstructor
public class CreateRepresentationMessage {
    public final List<Article> articles;
    public final Map<String, List<Double>> globalVectors;
    public final Boolean normalize;

    public static CreateRepresentationMessage of(List<Article> articles,Map<String, List<Double>> globalVectors ,Boolean normalize) {
        return new CreateRepresentationMessage(Collections.unmodifiableList(articles),
                Collections.unmodifiableMap(globalVectors),
                normalize);
    }

}
