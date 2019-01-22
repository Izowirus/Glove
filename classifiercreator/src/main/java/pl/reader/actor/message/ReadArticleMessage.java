package pl.reader.actor.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadArticleMessage {
    public final String fileName;

    public static ReadArticleMessage of(String fileName){
        return new ReadArticleMessage(fileName);
    }
}
