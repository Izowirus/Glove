package pl.reader.actor.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadVectorMessage {

    public final String fileName;
    public final Boolean useStopList;
    public final Integer stopListSize;

    public static ReadVectorMessage of(String fileName, Boolean useStopList, Integer stopListSize) {
        return new ReadVectorMessage(fileName, useStopList, stopListSize);
    }
}
