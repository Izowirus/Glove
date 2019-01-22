package pl.performace_tests.actor.message;

import lombok.AllArgsConstructor;
import pl.model.ArticleRepresentation;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class CrossTestClassificatorMessage {
    public final Collection<ArticleRepresentation> data;
    public final int batchNo;

    public static CrossTestClassificatorMessage of(Collection<? extends ArticleRepresentation> data, int batchNo) {
        return new CrossTestClassificatorMessage(Collections.unmodifiableCollection(data), batchNo);
    }
}
