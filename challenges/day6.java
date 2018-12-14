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
        AtomicInteger result = new AtomicInteger();

        Map<Integer, Pair<Integer, Integer>> coordinatesMap = getCoordinatesWithId();
            int[][] coordinates = getGridFromCoordinates(coordinatesMap);
            coordinatesMap.forEach((key, coordinate) -> coordinates[coordinate.getKey()][coordinate.getValue()] = key);

            for (int x = 0; x < coordinates.length; x++) {
                for (int y = 0; y < coordinates[0].length; y++) {
                    coordinates[x][y] = getIdSmallestManhattanDistance(x, y, coordinatesMap);
                }
            }
            Set<Integer> boundIds = new HashSet<>();
            for (int x = 0; x < coordinates.length; x++) {
                boundIds.add(coordinates[x][0]);
                boundIds.add(coordinates[x][coordinates[0].length-1]);
            }
            for (int y = 0; y < coordinates[0].length; y++) {
                boundIds.add(coordinates[0][y]);
                boundIds.add(coordinates[coordinates.length-1][y]);
            }
            result.set(getLargestFiniteArea(coordinates, boundIds));
        return result.get();
    }

    private static Map<Integer, Pair<Integer, Integer>> getCoordinatesWithId() {
        final AtomicInteger counter = new AtomicInteger();
        Map<Integer, Pair<Integer, Integer>> coordinatesMap = new HashMap<>();
        try {
            Files.lines(Paths.get("input/input_day6.txt"))
                    .forEach(fLine -> {
                        int xC = Integer.parseInt(fLine.substring(0, fLine.indexOf(",")));
                        int yC = Integer.parseInt(fLine.substring(fLine.indexOf(",") + 2, fLine.length()));
                        coordinatesMap.put(counter.get(), new Pair<>(xC, yC));
                        counter.incrementAndGet();
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coordinatesMap;
    }

    private static int[][] getGridFromCoordinates(Map<Integer, Pair<Integer, Integer>> coordinates) {
        AtomicInteger maxX = new AtomicInteger(-1);
        AtomicInteger maxY = new AtomicInteger(-1);
        coordinates.values()
                .forEach(coordinate -> {
                    if(coordinate.getKey() > maxX.get()) maxX.set(coordinate.getKey());
                    if(coordinate.getValue() > maxY.get()) maxY.set(coordinate.getValue());
                });
        return new int[maxX.get() + 1][maxY.get() + 1];
    }

    private static int getRegionLessManhattanDistances() {
        Map<Integer, Pair<Integer, Integer>> coordinateMap = getCoordinatesWithId();
        int[][] grid = getGridFromCoordinates(coordinateMap);
        final AtomicInteger counter = new AtomicInteger();
        AtomicInteger lessCounter = new AtomicInteger();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                int finalX = x;
                int finalY = y;
                coordinateMap.forEach((key, value) -> counter.addAndGet(getManhattanDistance(finalX, value.getKey(), finalY, value.getValue())));
                if(counter.get() < 10000) lessCounter.incrementAndGet();
                counter.set(0);
            }
        }
        return lessCounter.get();
    }

    private static int getManhattanDistance(final int x1, final int x2, int y1, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    private static int getIdSmallestManhattanDistance(int x, int y, Map<Integer, Pair<Integer, Integer>> coordinates) {
        AtomicInteger minManhattanDist = new AtomicInteger(1000);
        AtomicInteger manhattanId = new AtomicInteger();
        coordinates.forEach((key, coordinate) -> {
            int manhattanDist = getManhattanDistance(x, coordinate.getKey(), y, coordinate.getValue());
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
        System.out.println("The region with manhattan distance < 10000 is: " + getRegionLessManhattanDistances());
    }
}
