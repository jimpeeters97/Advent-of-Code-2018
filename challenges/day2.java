package challenges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class day2 {
    //first challenge
    private static int calculateCheckSum() {
        final AtomicInteger doubleHit = new AtomicInteger();
        final AtomicInteger tripleHit = new AtomicInteger();
        try {
            Files.lines(Paths.get("input/input_day2.txt"))
                    .forEach(fLine -> {
                        List<Character> allChars = fLine.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList());
                        Set<Character> uniqueChars = new HashSet<>(allChars);
                        boolean isDoubleHit = false;
                        boolean isTripleHit = false;

                        for(Character ch : uniqueChars) {
                            int freq = Collections.frequency(allChars, ch);
                            if(freq == 2 && !isDoubleHit) {
                                doubleHit.incrementAndGet();
                                isDoubleHit = true;
                            } else if(freq == 3 && !isTripleHit) {
                                tripleHit.incrementAndGet();
                                isTripleHit = true;
                            }
                        }
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return doubleHit.get() * tripleHit.get();
    }

    //second challenge
    private static String getCommonLetters() {
        try {
            List<String> boxesIDs = Files.lines(Paths.get("input/input_day2.txt")).collect(Collectors.toList());
            Optional<String> commonLetters = boxesIDs.stream()
                    .map(id -> {
                        Optional<String> duplicate = findDuplicateID(boxesIDs, id);
                            return duplicate.orElse("");
                    })
                    .filter(dup -> !dup.equals(""))
                    .findFirst();

            return commonLetters.orElse("Niet gevonden!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    private static Optional<String> findDuplicateID(final List<String> boxesIDs, final String boxID) {

        return boxesIDs.stream()
                .filter(id -> !id.equals(boxID))
                .map(id -> {
                    AtomicInteger difference = new AtomicInteger(0);
                    int i = 0;
                    String commonLetters = "";
                    while(i < boxID.length()) {
                        if(id.charAt(i) != boxID.charAt(i)) difference.incrementAndGet();
                        else commonLetters = commonLetters.concat(Character.toString(id.charAt(i)));
                        i++;
                    }
                    return difference.get() == 1 ? commonLetters : null;
                })
                .filter(Objects::nonNull)
                .findFirst();
    }

    public static void main(String[] args) {
        System.out.println(String.format("The checksum is: %s", calculateCheckSum()));
        System.out.println(String.format("The common letters are: %s", getCommonLetters()));
    }
}
