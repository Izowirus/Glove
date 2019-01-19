package pl.performace_tests.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import pl.classification.Classificator;
import pl.model.ArticleRepresentation;
import pl.model.Category;
import pl.performace_tests.ClassificationResults;
import pl.performace_tests.ClassificationTestLogger;
import pl.performace_tests.actor.message.SplitRequestMessage;
import pl.performace_tests.actor.message.SplitResponseMessage;
import pl.performace_tests.actor.message.CrossTestClassificatorMessage;
import pl.performace_tests.actor.message.TestClassificatorMessage;
import scala.concurrent.Future;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class CrossValidationActor extends AbstractActor {

    private Classificator<ArticleRepresentation> classificator;

    public static Props props(Classificator<ArticleRepresentation> classificator) {
        return Props.create(CrossValidationActor.class, () -> new CrossValidationActor(classificator));
    }

    @Override
    public void preStart() {
        System.out.println("CrossValidationActor started");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CrossTestClassificatorMessage.class, this::requestSplitting)
                .match(SplitResponseMessage.class, this::requestValidation)
                .build();
    }

    private void requestValidation(SplitResponseMessage response) {
        List<List<ArticleRepresentation>> splitedData = response.splitedData;
        Collection<ArticleRepresentation> data = new ArrayList<>(response.data);

        List<CompletionStage<Object>> stages = splitedData.stream()
                .map(test -> {
                    Collection<ArticleRepresentation> train = CollectionUtils.disjunction(data, test);
                    return Pair.of(train, test);
                })
                .map(pair -> {
                    ActorRef actorRef = getContext().actorOf(ValidatorActor.props(classificator));
                    return Patterns.ask(actorRef,
                            TestClassificatorMessage.of(pair.getLeft(), pair.getRight()),
                            Duration.ofMinutes(10));
                }).collect(Collectors.toList());


        List<ClassificationResults> results = stages.stream()
                .map(CompletionStage::toCompletableFuture)
                .map(this::getFromFuture)
                .collect(Collectors.toList());


        results.forEach(ClassificationTestLogger::printBatchResults);
        ClassificationResults result = sumResults(results);
        ClassificationTestLogger.printCategoriesStats(result);
        ClassificationTestLogger.printClassificationResults(result);

    }

    private ClassificationResults sumResults(Collection<ClassificationResults> results) {
        int categotiesNo = Category.values().length;
        int[] allArticlesNums = new int[categotiesNo];
        int[] allArticlesProperClassifiesNums = new int[categotiesNo];
        results.forEach(result ->
                IntStream.range(0, categotiesNo).forEach(catIdx -> {
                    allArticlesNums[catIdx] += result.catArticlesNums[catIdx];
                    allArticlesProperClassifiesNums[catIdx] += result.catArticlesProperClassifiesNums[catIdx];
                })
        );
        return new ClassificationResults(allArticlesNums, allArticlesProperClassifiesNums);
    }

    private ClassificationResults getFromFuture(CompletableFuture<Object> future) {
        try {
            return (ClassificationResults) future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void requestSplitting(CrossTestClassificatorMessage message) {
        Collection<ArticleRepresentation> data = message.data;
        int batchNo = message.batchNo;
        ActorRef ref = getContext().actorOf(ArticleSplitterActor.props());
        ref.tell(SplitRequestMessage.of(data, batchNo), getSelf());
    }
}
