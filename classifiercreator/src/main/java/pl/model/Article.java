package pl.model;

public class Article {

    private String topic;
    private String content;
    private String title;

    public Article() {
    }

    public Article(String topic, String content, String title) {
        this.topic = topic;
        this.content = content;
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
