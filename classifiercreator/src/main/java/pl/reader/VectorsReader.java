package pl.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;

public class VectorsReader {

    private Map<String, List<Double>> globalVectors = new HashMap<>();

    public VectorsReader(Path vectorsPath) throws IOException {
        readVectorsFile(vectorsPath);
    }

    public VectorsReader(Path vectorsPath, int stopListSize) throws IOException {
        readVectorsWithoutStoplist(vectorsPath, stopListSize);
    }

    private void readVectorsFile(Path vectorsPath) throws IOException {
            Files.readAllLines(vectorsPath).forEach(this::processLine);
    }

    private void readVectorsWithoutStoplist(Path vectorsPath, int stopListSize) throws IOException {
        int i = 0;
       for(String line : Files.readAllLines(vectorsPath)){
           if(i >= stopListSize){
               processLine(line);
           }
           i++;
       }
    }

    private void processLine(String line) {
        List<String> parsedLine = Arrays.asList(line.split(SPACE));
        String word = parsedLine.get(0);
        List<Double> vector = parsedLine.subList(1, parsedLine.size()).stream().map(Double::valueOf).collect(Collectors.toList());
        globalVectors.put(word, vector);
    }

    public Map<String, List<Double>> getGlobalVectors() {
        return globalVectors;
    }
}