package pl.reader.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import pl.LoggerActor;
import pl.performace_tests.actor.ArticleSplitterActor;
import pl.reader.VectorsReader;
import pl.reader.actor.message.ReadVectorMessage;
import pl.reader.actor.message.VectorsMessage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class VectorsReaderActor extends LoggerActor {

    public static Props props() {
        return Props.create(VectorsReaderActor.class, VectorsReaderActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReadVectorMessage.class, this::readVectors)
                .build();
    }

    private void readVectors(ReadVectorMessage message) {
        final Path path = Paths.get(message.fileName);
        final VectorsReader vectorsReader = createVectorsReader(path, message.useStopList, message.stopListSize);
        final Map<String, List<Double>> globalVectors = vectorsReader.getGlobalVectors();
        getSender().tell(VectorsMessage.of(globalVectors), getSelf());
    }

    private VectorsReader createVectorsReader(Path path, Boolean useStopList, Integer stopListSize) {
        try {
            if (useStopList) {
                return new VectorsReader(path, stopListSize);
            } else {

                return new VectorsReader(path);
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }
}
