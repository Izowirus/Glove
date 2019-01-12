package pl.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    COUNTRY(7),
    CULTURE(1),
    CURIOSITIES(2),
    ECONOMY(3),
    HEALTH(4),
    SCIENCE(5),
    SPORT(6),
    WORLD(0);

    private int label;

    Category(int intLabelValue){
        this.label = intLabelValue;
    }

    public int getLabel() {
        return label;
    }

    public static Category valueOfInt(int label){
       return Arrays.stream(Category.values())
                .filter(category -> category.getLabel() == label)
                .findAny()
                .orElse(null);
    }

    public static Category valueOfString(String categoryName){
        return Arrays.stream(Category.values())
                .filter(category -> category.name().equals(categoryName))
                .findAny()
                .orElse(null);
    }

    public static List<String> categoriesNames() {
        return Arrays.stream(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
