package pl.performace_tests.actor.message;

import lombok.AllArgsConstructor;
import pl.model.ArticleRepresentation;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class SplitRequestMessage {
    public final Collection<ArticleRepresentation> data;
    public final int batchNo;

    public static SplitRequestMessage of(Collection<? extends ArticleRepresentation> data, int batchNo) {
        return new SplitRequestMessage(Collections.unmodifiableCollection(data), batchNo);
    }
}
