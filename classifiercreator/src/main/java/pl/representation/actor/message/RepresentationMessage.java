package pl.representation.actor.message;

import lombok.AllArgsConstructor;
import pl.model.ArticleRepresentation;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class RepresentationMessage {
    public final List<ArticleRepresentation> representation;

    public static RepresentationMessage of(List<ArticleRepresentation> representation){
        return new RepresentationMessage(Collections.unmodifiableList(representation));
    }
}
