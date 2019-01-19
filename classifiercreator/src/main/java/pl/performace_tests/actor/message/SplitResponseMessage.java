package pl.performace_tests.actor.message;

import lombok.AllArgsConstructor;
import pl.model.ArticleRepresentation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class SplitResponseMessage {
    public final List<List<ArticleRepresentation>> splitedData;
    public final Collection<ArticleRepresentation> data;

    public static SplitResponseMessage of(List<List<ArticleRepresentation>> splitedData,
                                          Collection<ArticleRepresentation> data) {
        return new SplitResponseMessage(Collections.unmodifiableList(splitedData),
                Collections.unmodifiableCollection(data));
    }
}
