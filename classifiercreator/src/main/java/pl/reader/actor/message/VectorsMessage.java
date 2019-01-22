package pl.reader.actor.message;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class VectorsMessage {
    public final Map<String, List<Double>> vectors;

    public static VectorsMessage of(Map<String, List<Double>> vectors){
        return new VectorsMessage(Collections.unmodifiableMap(vectors));
    }
}
