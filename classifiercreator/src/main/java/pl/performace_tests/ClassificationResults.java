package pl.performace_tests;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClassificationResults {
    public final int[] catArticlesNums;
    public final int[] catArticlesProperClassifiesNums;
}