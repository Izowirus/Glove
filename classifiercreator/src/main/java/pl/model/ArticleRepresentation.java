package pl.model;

import java.util.List;

public class ArticleRepresentation {
    private Integer label;
    private List<Double> vector;
    private String title;

    public ArticleRepresentation(Integer label, List<Double> vector, String title) {
        this.label = label;
        this.vector = vector;
        this.title = title;
    }

    public ArticleRepresentation() {
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public List<Double> getVector() {
        return vector;
    }

    public void setVector(List<Double> vector) {
        this.vector = vector;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ArticleRepresentation{" +
                "label=" + label +
                ", vector=" + vector +
                ", title='" + title + '\'' +
                '}';
    }
}
