package pl.performace_tests.actor.message;

import lombok.AllArgsConstructor;
import pl.performace_tests.ClassificationResults;

@AllArgsConstructor
public class ClassifiactionResultsMessage {
    public ClassificationResults classificationResults;
    public int batchId;
}
