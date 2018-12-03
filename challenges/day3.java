package challenges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class day3 {

    private static int calculateClaims() {
        AtomicInteger result = new AtomicInteger();
            Map<List<Integer>, List<Integer>> claimPositions = calculatePositions();
            claimPositions.values()
                    .forEach(pos -> {
                        if(pos.size() > 1) {
                            result.incrementAndGet();
                        }
                    });
        return result.get();
    }

    private static Map<List<Integer>, List<Integer>> calculatePositions() {
        Map<List<Integer>, List<Integer>> claimPositions = new HashMap<>();
        try {
            Files.lines(Paths.get("input/input_day3.txt"))
                    .forEach(fLine -> {
                        int id = Integer.parseInt(fLine.substring(1, fLine.indexOf("@") - 1));
                        int leftSpace = Integer.parseInt(fLine.substring(fLine.indexOf("@") + 2, fLine.indexOf(",")));
                        int topSpace = 1000 - Integer.parseInt(fLine.substring(fLine.indexOf(",") + 1, fLine.indexOf(":")));
                        int width = Integer.parseInt(fLine.substring(fLine.indexOf(":") + 2, fLine.indexOf("x")));
                        int height = Integer.parseInt(fLine.substring(fLine.indexOf("x") + 1, fLine.length()));

                        for (int x = 0; x < width; x++) {
                            for(int y = 0; y < height; y++) {
                                List<Integer> position = Arrays.asList(leftSpace + x, topSpace - y);
                                List<Integer> newList = new ArrayList<>();
                                if(claimPositions.containsKey(position)) {
                                    newList = claimPositions.get(position);
                                }
                                newList.add(id);
                                claimPositions.put(position, newList);
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return claimPositions;
    }

    private static Integer calculateNonDuplicateClaim() {
        List<Integer> ids = new ArrayList<>();
        try {
            Files.lines(Paths.get("input/input_day3.txt")).forEach(fLine -> {
                int id = Integer.parseInt(fLine.substring(1, fLine.indexOf("@") - 1));
                ids.add(id);
            });
            Map<List<Integer>, List<Integer>> claims = calculatePositions();
            claims.values().forEach(claim -> claim
                    .forEach(claimId -> {
                        if(claim.size() > 1 && ids.contains(claimId)) {
                            ids.remove(claimId);
                        }
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids.get(0);
    }

    public static void main(String[] args) {
        System.out.println("The amount of claims is: " + calculateClaims());
        System.out.println("The non-duplicate claim is: " + calculateNonDuplicateClaim());
    }
}
