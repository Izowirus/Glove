package pl.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public enum TopicLabel {
    COUNTRY(0),
    CULTURE(1),
    CURIOSITIES(2),
    ECONOMY(3),
    HEALTH(4),
    SCIENCE(5),
    SPORT(6),
    WORLD(7);

    private int label;

    TopicLabel(int label){
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public static TopicLabel valueOf(int label){
       return Arrays.stream(TopicLabel.values())
                .filter(topic -> topic.getLabel() == label)
                .findAny()
                .orElse(null);
    }

    public static Collection<String> topicNames() {
        return Arrays.stream(TopicLabel.values())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
