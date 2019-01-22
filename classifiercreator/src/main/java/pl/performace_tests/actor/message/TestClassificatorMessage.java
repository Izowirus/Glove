package pl.performace_tests.actor.message;

import lombok.AllArgsConstructor;
import pl.model.ArticleRepresentation;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class TestClassificatorMessage {
    public final Collection<ArticleRepresentation> train;
    public final Collection<ArticleRepresentation> test;
    public final int batchId;

    public static TestClassificatorMessage of(Collection<ArticleRepresentation> train,
                                              Collection<ArticleRepresentation> test, int batchId) {
        return new TestClassificatorMessage(Collections.unmodifiableCollection(train),
                Collections.unmodifiableCollection(test), batchId);
    }
}
