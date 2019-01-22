package pl.representation.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.AllArgsConstructor;
import pl.LoggerActor;
import pl.model.ArticleRepresentation;
import pl.representation.ArticleRepresentationService;
import pl.representation.VectorNormalizer;
import pl.representation.actor.message.CreateRepresentationMessage;
import pl.representation.actor.message.RepresentationMessage;

import java.util.List;

@AllArgsConstructor
public class ArticleRepresentationActor extends LoggerActor {

    public static Props props(ArticleRepresentationService service, VectorNormalizer normalizer){
        return Props.create(ArticleRepresentationActor.class, () -> new ArticleRepresentationActor(service, normalizer));
    }

    private ArticleRepresentationService articleRepresentationService;
    private VectorNormalizer normalizer;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateRepresentationMessage.class, this::createRepresentation)
                .build();
    }

    private void createRepresentation(CreateRepresentationMessage message) {
        final List<ArticleRepresentation> representation = articleRepresentationService.createRepresentation(message.articles, message.globalVectors);
        if(message.normalize){
            normalizer.normalize(representation);
        }
        getSender().tell(RepresentationMessage.of(representation), getSelf());
    }
}
