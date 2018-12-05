package challenges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class day5 {

    private static int calculateUnitLength(String charString) throws IOException {
        AtomicInteger result = new AtomicInteger();
            Files.lines(Paths.get("input/input_day5.txt"))
                    .forEach(fLine -> {
                        List<Character> chars = charString.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                        boolean duplicateFound = true;
                        while(duplicateFound) {
                            duplicateFound = false;
                            for (int i = 0; i < chars.size() - 1; i++) {
                                boolean differentCasing = (Character.isUpperCase(chars.get(i)) && Character.isLowerCase(chars.get(i + 1))) || (Character.isLowerCase(chars.get(i)) && Character.isUpperCase(chars.get(i + 1)));
                                if(Character.toLowerCase(chars.get(i)) == Character.toLowerCase(chars.get(i + 1)) && differentCasing) {
                                    chars.remove(i);
                                    chars.remove(i);
                                    i--;
                                    duplicateFound = true;
                                }
                            }
                        }
                        result.set(chars.size());
                    });
        return result.get();
    }

    private static int calculateLengthShortestPolymer() throws IOException {
        AtomicInteger result = new AtomicInteger(999999);
        getAllPolymers().forEach(polymer -> {
            try {
                Files.lines(Paths.get("input/input_day5.txt"))
                        .forEach(fLine -> {
                            String polymerString = fLine.replace(polymer.toString().toLowerCase(), "");
                            polymerString = polymerString.replace(polymer.toString().toUpperCase(), "");
                            try {
                                int polySize = calculateUnitLength(polymerString);
                                if(polySize < result.get()) {
                                    result.set(polySize);
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result.get();
    }

    private static Set<Character> getAllPolymers() throws IOException {
            return Files.lines(Paths.get("input/input_day5.txt"))
                    .map(fLine -> fLine.chars().mapToObj(c -> (char) c).collect(Collectors.toSet()))
                    .collect(Collectors.toList())
                    .get(0);
    }

    public static void main(String[] args) throws IOException {
        Files.lines(Paths.get("input/input_day5.txt"))
                .forEach(fLine -> {
                    try {
                        System.out.println("The length of the unit is: " + calculateUnitLength(fLine));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println("The length of the shortest polymer is: " + calculateLengthShortestPolymer());
    }
}
