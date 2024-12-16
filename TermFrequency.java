import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TermFrequency {

    public static void main(String[] args) {
        readFile(args[0], 
            (text) -> removeNonAlpha(text, 
                (textNoAlpha) -> removeSingleLetters(textNoAlpha,
                    (textNoSingleLetter) -> convertLowerCase(textNoSingleLetter,
                        (textInLower) -> splitIntoArray(textInLower, 
                            (arrayOfWord) -> toListWord(arrayOfWord,
                                (streamOfWord) -> readFile("stop_words.txt", 
                                    (textOfStopWord) -> splitIntoArrayST(textOfStopWord,
                                        (arrayOfStopWord) -> toListStopWord(arrayOfStopWord,
                                            (listOfStopWord) -> removeStopWords(streamOfWord, listOfStopWord, 
                                                (cleanedList) -> countFrequency(cleanedList, 
                                                    (mapCoupleWordFreq) -> sort(mapCoupleWordFreq, 
                                                        (listmapCoupleWordFreqSorted) -> findMostFrequencyWord(listmapCoupleWordFreqSorted, getLimit(args[1]),
                                                            (nFreqWord) ->  printAll(nFreqWord,
                                                            () -> System.out.println()
                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    ) 
                                )
                            )
                        )
                    ) 
                )
            )
        );
    }

    //method to read a file
    private static void readFile(String pathToFile, Consumer<String> continuation) {
        try {
            continuation.accept(Files.readString(Paths.get(pathToFile), StandardCharsets.UTF_8));
        } catch(final IOException e) {
            e.printStackTrace();
        }
    }

    //method to remove nonAlphaNumericWords
    private static void removeNonAlpha(String text, Consumer<String> continuation) {
        continuation.accept(text.replaceAll("[\\W_]+", " "));
    }

     //method to remove single word
     private static void removeSingleLetters(String textNoAlpha, Consumer<String> continuation) {
        continuation.accept(textNoAlpha.replaceAll("\\b[a-zA-Z]\\b", "").replaceAll("\\s{2,}", " ").trim());
    }

    //method to put all in lowerCase
    private static void convertLowerCase(String textNoSingleLetter, Consumer<String> continuation) {
        continuation.accept(textNoSingleLetter.toLowerCase());  
    }

    //method to split the string into an array
    private static void splitIntoArray(String textInLower, Consumer<String[]> continuation) {
        continuation.accept(textInLower.split(" "));
    }

    private static void splitIntoArrayST(String stopWord, Consumer<String[]> continuation) {
        continuation.accept(Arrays.stream(stopWord.split(","))
                        .map(String::trim)
                        .toArray(String[]::new));
    }
    
    //converting array into a list
    private static void toListWord(String[] arrayOfWord, Consumer<Stream<String>> continuation) {
        continuation.accept(Arrays.stream(arrayOfWord));
    }

    //INSERIRE UNO SPLIT SULLE STOPWORD PER ,

    //stop word management

        //transform stopWord into an Array 
        private static void toListStopWord(String[] arrayOfStopWord, Consumer<List<String>> continuation) {
            continuation.accept(Arrays.asList(arrayOfStopWord));
        }

        //removing stopWord from the file
        private static void removeStopWords(Stream<String> streamOfWord, List<String> listOfStopWord, Consumer<List<String>> continuation) {
            continuation.accept(streamOfWord
                .filter(word -> !(listOfStopWord).contains(word))
                .collect(Collectors.toUnmodifiableList()));
        }

    //counting word
    private static void countFrequency(List<String> cleanedList, Consumer<Map<String, Integer>> continuation) {
        continuation.accept(cleanedList.stream().collect(Collectors.groupingBy(
            word -> word, //word is the same
            Collectors.reducing(0, word -> 1, Integer::sum) //count that start from zero, and add one for each occurrence of the word
        )));
    }

    //sorting the word (entry transform the map into a set)
    private static void sort(Map<String, Integer> mapCoupleWordFreq, Consumer<List<Map.Entry<String, Integer>>> continuation) {
        continuation.accept(mapCoupleWordFreq.entrySet().stream()
            .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Ordinamento decrescente
            .collect(Collectors.toList()));
    }

    //extract N most frequency word
    private static void findMostFrequencyWord(List<Map.Entry<String, Integer>> listmapCoupleWordFreqSorted, int limit, Consumer<List<Map.Entry<String, Integer>>> continuation) {
        continuation.accept(listmapCoupleWordFreqSorted.subList(0, Math.min(limit, listmapCoupleWordFreqSorted.size())));
    }

    //print the results
    private static void printAll(List<Map.Entry<String, Integer>> list, Runnable continuation) {
        if (list.size() > 0) {
            System.out.println(list.get(0).getKey() + "  -  " + list.get(0).getValue());
            printAll(list.subList(1, list.size()), continuation);
        } else {
            continuation.run();
        } 
    }

    //get limit 
    private static int getLimit(String limit){
        return Integer.parseInt(limit);
    }
}
