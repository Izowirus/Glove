package pl.classification.article;

import pl.classification.Classificator;
import pl.model.ArticleRepresentation;

public class ArticleRepresentationClassificatorFactory{

    public Classificator<ArticleRepresentation> KNNClassificator(){
        return new KNNClassificator();
    }

    public Classificator<ArticleRepresentation> KNNClassificator(int k){
        return new KNNClassificator(k);
    }

    public Classificator<ArticleRepresentation> SVMClassificator(){
        return new SVMClassificator(1, 50);
    }

    public Classificator<ArticleRepresentation> SVMClassificator(double sigma, double C){
        return new SVMClassificator(sigma, C);
    }

    public Classificator<ArticleRepresentation> AdaBoostClassificator(){
        return new AdaBoostClassificator();
    }

    public Classificator<ArticleRepresentation> NNClassificator(int... layes){
        return new NeuralNetworkClassificator(200, 0.05, layes);
    }

    public Classificator<ArticleRepresentation> NNClassificator(int epochs, int... layes){
        return new NeuralNetworkClassificator(epochs, 0.05, layes);
    }



}
