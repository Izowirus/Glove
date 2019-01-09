package pl.edu.pw.elka.stud.wedtsag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleReprCreator {

    private static final String SPACE = " ";

    private Map<String, List<Float>> globalVectors;
    private Map<String, Integer> vocabWithOccurrences;

    public ArticleReprCreator(Path vectorsPath, Path vocabPath) {
        readVectorsFile(vectorsPath);
        readVocabFile(vocabPath);
    }

    private void readVectorsFile(Path vectorsPath) {
        globalVectors = new HashMap<>();
        try {
            Files.readAllLines(vectorsPath).forEach(line -> {
                List<String> parsedLine = Arrays.asList(line.split(SPACE));
                String word = parsedLine.get(0);
                List<Float> vector = parsedLine.subList(1, parsedLine.size()).stream().map(Float::valueOf).collect(Collectors.toList());
                globalVectors.put(word, vector);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readVocabFile(Path vocabPath) {
        vocabWithOccurrences = new HashMap<>();
        try {
            Files.readAllLines(vocabPath).forEach(line -> {
                String[] parsedLine = line.split(SPACE);
                vocabWithOccurrences.put(parsedLine[0], Integer.valueOf(parsedLine[1]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Float> getRepresentation(String article) {
        return null;
    }


}
