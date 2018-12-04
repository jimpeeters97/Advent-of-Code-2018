package challenges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class day4 {
    //first challenge
    private static int calculateGuard() {
        AtomicInteger result = new AtomicInteger();
        AtomicInteger guardId = new AtomicInteger();
        Map<Integer, List<Integer>> guardSleepMinutes = getGuardsWithSleepMinutes();
        guardSleepMinutes.forEach((key, value) -> {
            if (value.size() > result.get()) {
                guardId.set(key);
                result.set(value.size());
            }
        });

        int frequentMinute = getMostFrequentMinuteWithFrequency(guardSleepMinutes, guardId.get())[0];
        result.set(guardId.get() * frequentMinute);
        return result.get();
    }

    private static Map<Integer, List<Integer>> getGuardsWithSleepMinutes() {
        AtomicInteger guardId = new AtomicInteger();
        AtomicInteger guardAsleep = new AtomicInteger();
        AtomicInteger guardAwake = new AtomicInteger();
        Map<Integer, List<Integer>> guardSleepMinutes = new HashMap<>();
        try {
            List<Integer> sleepMinutes = new ArrayList<>();
            Files.lines(Paths.get("input/input_day4.txt"))
                    .sorted()
                    .forEach(fLine -> {
                        int minute = Integer.parseInt(fLine.substring(15, 17));
                        String action = fLine.substring(19, fLine.length());
                        if (action.equals("falls asleep")) {
                            guardAsleep.set(minute);
                        } else if (action.equals("wakes up")) {
                            guardAwake.set(minute);
                            int range = guardAwake.get() - guardAsleep.get();
                            for (int r = 0; r < range; r++) {
                                sleepMinutes.add(guardAsleep.get() + r);
                            }
                        } else if (action.contains("Guard #")) {
                            if (guardSleepMinutes.containsKey(guardId.get())) {
                                List<Integer> newList = new ArrayList<>(guardSleepMinutes.get(guardId.get()));
                                newList.addAll(sleepMinutes);
                                guardSleepMinutes.put(guardId.get(), newList);
                            } else {
                                guardSleepMinutes.put(guardId.get(), sleepMinutes);
                            }
                            guardId.set(Integer.parseInt(fLine.substring(fLine.indexOf("#") + 1, fLine.indexOf("b") - 1)));
                            sleepMinutes.clear();
                        }
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return guardSleepMinutes;
    }

    private static int[] getMostFrequentMinuteWithFrequency(Map<Integer, List<Integer>> guardSleepMinutes, int guardId) {
        AtomicInteger highestFrequency = new AtomicInteger();
        AtomicInteger frequentMinute = new AtomicInteger();
        guardSleepMinutes.get(guardId).stream()
                .distinct()
                .sorted()
                .forEach(minute -> {
                    if (Collections.frequency(guardSleepMinutes.get(guardId), minute) >= highestFrequency.get()) {
                        highestFrequency.set(Collections.frequency(guardSleepMinutes.get(guardId), minute));
                        frequentMinute.set(minute);
                    }
                });
        return new int[]{frequentMinute.get(), highestFrequency.get()};
    }

    //second challenge
    private static int calculateFrequencySameMinute() {
        AtomicInteger result = new AtomicInteger();
        Map<Integer, List<Integer>> guardSleepMinutes = getGuardsWithSleepMinutes();
        AtomicInteger highestFrequency = new AtomicInteger();
        AtomicInteger sleepiestMinute = new AtomicInteger();
        guardSleepMinutes.forEach((key, value) -> {
            int[] frequencyArray = getMostFrequentMinuteWithFrequency(guardSleepMinutes, key);
            sleepiestMinute.set(frequencyArray[0]);
            if (frequencyArray[1] > highestFrequency.get()) {
                highestFrequency.set(frequencyArray[1]);
                result.set(key * sleepiestMinute.get());
            }
        });
        return result.get();
    }

    public static void main(String[] args) {
        System.out.println("The result of GuardID * minute is: " + calculateGuard());
        System.out.println("The result of GuardID * same minute is: " + calculateFrequencySameMinute());
    }
}
