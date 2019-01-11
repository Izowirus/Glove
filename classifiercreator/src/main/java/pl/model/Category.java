package pl.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    COUNTRY(0),
    CULTURE(1),
    CURIOSITIES(2),
    ECONOMY(3),
    HEALTH(4),
    SCIENCE(5),
    SPORT(6),
    WORLD(7);

    private int intValue;

    Category(int intLabelValue){
        this.intValue = intLabelValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public static Category valueOfInt(int label){
       return Arrays.stream(Category.values())
                .filter(category -> category.getIntValue() == label)
                .findAny()
                .orElse(null);
    }

    public static Category valueOfString(String label){
        return Arrays.stream(Category.values())
                .filter(category -> category.name().equals(label))
                .findAny()
                .orElse(null);
    }

    public static List<String> categoriesNames() {
        return Arrays.stream(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
