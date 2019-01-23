package pl.performace_tests.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import pl.LoggerActor;
import pl.classification.Classificator;
import pl.model.ArticleRepresentation;
import pl.model.Category;
import pl.performace_tests.ClassificationResults;
import pl.performace_tests.ClassificationTestLogger;
import pl.performace_tests.actor.message.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrossValidationActor extends LoggerActor {

    private Classificator<ArticleRepresentation> classificator;
    private ClassificationResults classificationResults;
    private int allBatches;
    private int aggregatedBatches;

    public static Props props(Classificator<ArticleRepresentation> classificator) {
        return Props.create(CrossValidationActor.class, () -> new CrossValidationActor(classificator));
    }

    public CrossValidationActor(Classificator<ArticleRepresentation> classificator) {
        this.classificator = classificator;
        int categoriesNo = Category.values().length;
        int[] allArticlesNums = new int[categoriesNo];
        int[] allArticlesProperClassifiesNums = new int[categoriesNo];
        classificationResults = new ClassificationResults(allArticlesNums, allArticlesProperClassifiesNums);

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CrossTestClassificatorMessage.class, this::requestSplitting)
                .match(SplitResponseMessage.class, this::requestValidation)
                .match(ClassifiactionResultsMessage.class, this::receiveBatchResults)
                .match(FailureClassificationMessage.class, this::handleFailure)
                .build();
    }

    private void requestValidation(SplitResponseMessage response) {
        List<List<ArticleRepresentation>> splitedData = response.splitedData;
        allBatches = response.splitedData.size();
        Collection<ArticleRepresentation> data = new ArrayList<>(response.data);

        List<Pair> pairs = splitedData
                .stream()
                .map(test -> {
                    Collection<ArticleRepresentation> train = CollectionUtils.disjunction(data, test);
                    return Pair.of(train, test);
                })
                .collect(Collectors.toList());

        IntStream.range(0, pairs.size()).forEach(idx -> {
            Pair<List<ArticleRepresentation>, List<ArticleRepresentation>> pair = pairs.get(idx);
            ActorRef actorRef = getContext().actorOf(ValidatorActor.props(classificator));
            actorRef.tell(TestClassificatorMessage.of(pair.getLeft(), pair.getRight(), idx), getSelf());
        });

    }

    private void requestSplitting(CrossTestClassificatorMessage message) {
        Collection<ArticleRepresentation> data = message.data;
        int batchNo = message.batchNo;
        ActorRef ref = getContext().actorOf(ArticleSplitterActor.props());
        ref.tell(SplitRequestMessage.of(data, batchNo), getSelf());
    }

    private void receiveBatchResults(ClassifiactionResultsMessage message) {
        aggregatedBatches += 1;
        agregateResults(message.classificationResults);
        ClassificationTestLogger.printBatchResults(message.classificationResults, message.batchId);
        if (aggregatedBatches == allBatches) {
            ClassificationTestLogger.printClassificationResults(classificationResults);
            ClassificationTestLogger.printCategoriesStats(classificationResults);
        } else {
            ClassificationTestLogger.printAgregatedResults(classificationResults, aggregatedBatches, allBatches);
        }
    }

    private void agregateResults(ClassificationResults results) {
        IntStream.range(0, Category.values().length).forEach(catIdx -> {
            classificationResults.catArticlesNums[catIdx] += results.catArticlesNums[catIdx];
            classificationResults.catArticlesProperClassifiesNums[catIdx] += results.catArticlesProperClassifiesNums[catIdx];
        });
    }

    private void handleFailure(FailureClassificationMessage failure){
        System.out.printf("Batch with id %d processing failed, cause: %s", failure.testClassificatorMessage.batchId, failure.failureCause);
        System.out.println("Retrying...");
        ActorRef actorRef = getContext().actorOf(ValidatorActor.props(classificator));
        actorRef.tell(failure.testClassificatorMessage, getSelf());
    }
}
