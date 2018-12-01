package challenges;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class day1 {
    //first challenge
    public static int calculateResult() {
        final AtomicInteger result = new AtomicInteger();
        try {
            Files.lines(Paths.get("input/input_day1.txt"))
                    .forEach(fLine -> {
                        result.getAndSet(calculateSubResult(result, fLine));
                    });
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result.get();
    }

    //second challenge
    public static int calculateRepeatFrequency() {
        final AtomicInteger result = new AtomicInteger();
        List<Integer> resultList = new ArrayList<>();
        boolean isDouble = false;

        while(!isDouble) {
            try {
                List<String> fileLines = Files.lines(Paths.get("input/input_day1.txt")).collect(Collectors.toList());
                for(String fLine : fileLines) {
                    resultList.add(calculateSubResult(result, fLine));
                    if(Collections.frequency(resultList, result.get()) > 1) {
                        isDouble = true;
                        return result.get();
                    }
                }
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    @NotNull
    private static int calculateSubResult(@NotNull final AtomicInteger atomicInt,
                                          @NotNull final String frequencyLine) {
        int lineFreq = Integer.parseInt(frequencyLine.substring(1));
        if(frequencyLine.startsWith("-")) {
            atomicInt.getAndSet(atomicInt.get() - lineFreq);
        } else {
            atomicInt.getAndAdd(lineFreq);
        }

        return atomicInt.get();
    }

    public static void main(String[] args) {
        System.out.println(String.format("The result frequency is: %s", calculateResult()));
        System.out.println(String.format("The frequency that is first reached twice is: %s", calculateRepeatFrequency()));
    }
}

