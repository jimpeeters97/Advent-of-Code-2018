package challenges;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class day6 {

    private static int getCoordinatesAsGrid() {
        AtomicInteger minX = new AtomicInteger(1000);
        AtomicInteger maxX = new AtomicInteger(-1);
        AtomicInteger minY = new AtomicInteger(1000);
        AtomicInteger maxY = new AtomicInteger(-1);
        AtomicInteger result = new AtomicInteger();

        Map<Integer, Pair<Integer, Integer>> coordinatesMap = new HashMap<>();
        try {
            AtomicInteger counter = new AtomicInteger();
            Files.lines(Paths.get("input/input_day6.txt"))
                    .forEach(fLine -> {
                        int xC = Integer.parseInt(fLine.substring(0, fLine.indexOf(",")));
                        int yC = Integer.parseInt(fLine.substring(fLine.indexOf(",") + 2, fLine.length()));
                        if(xC < minX.get()) minX.set(xC);
                        else if(xC > maxX.get()) maxX.set(xC);

                        if(yC < minY.get()) minY.set(yC);
                        else if(yC > maxY.get()) maxY.set(yC);

                        coordinatesMap.put(counter.get(), new Pair<>(xC, yC));
                        counter.incrementAndGet();
                    });


            int[][] coordinates = new int[maxX.get() + 1][maxY.get() + 1];
            coordinatesMap.forEach((key, coordinate) -> coordinates[coordinate.getKey()][coordinate.getValue()] = key);

            for (int x = 0; x <= maxX.get(); x++) {
                for (int y = 0; y <= maxY.get(); y++) {
                    coordinates[x][y] = getIdSmallestManhattanDistance(x, y, coordinatesMap);
                }
            }
            Set<Integer> boundIds = new HashSet<>();
            for (int x = 0; x <= maxX.get(); x++) {
                boundIds.add(coordinates[x][0]);
                boundIds.add(coordinates[x][maxY.get()]);
            }
            for (int y = 0; y <= maxY.get(); y++) {
                boundIds.add(coordinates[0][y]);
                boundIds.add(coordinates[maxX.get()][y]);
            }
            result.set(getLargestFiniteArea(coordinates, boundIds));
            //bounding box (grid) opmaken
            //abs(x2 - x1) + abs(y2 - y1) => voor elk punt
            //nieuwe grid opmaken met alle ids
            //
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result.get();
    }

    private static int getIdSmallestManhattanDistance(int x, int y, Map<Integer, Pair<Integer, Integer>> coordinates) {
        AtomicInteger minManhattanDist = new AtomicInteger(1000);
        AtomicInteger manhattanId = new AtomicInteger();
        coordinates.forEach((key, coordinate) -> {
            int manhattanDist = Math.abs(coordinate.getKey() - x) + Math.abs(coordinate.getValue() - y);
            if (manhattanDist == minManhattanDist.get()) manhattanId.set(-1);
            else if (manhattanDist < minManhattanDist.get()) {
                minManhattanDist.set(manhattanDist);
                manhattanId.set(key);
            }
        });
        return manhattanId.get();
    }

    private static int getLargestFiniteArea(int[][] coordinates, Set<Integer> infiniteIds) {
        Map<Integer, Integer> areas = new HashMap<>();
        for (int[] coordinate : coordinates) {
            for (int y = 0; y < coordinates[0].length; y++) {
                if (!infiniteIds.contains(coordinate[y])) {
                    if (areas.get(coordinate[y]) == null) areas.put(coordinate[y], 1);
                    else areas.put(coordinate[y], areas.get(coordinate[y]) + 1);
                }
            }
        }
        List<Integer> sizeList = areas.values().stream()
                .sorted()
                .collect(Collectors.toList());

        return sizeList.get(sizeList.size() - 1);
    }

    public static void main(String[] args) {
        System.out.println("The size of the largest area is: " + getCoordinatesAsGrid());
    }
}
