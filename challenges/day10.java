package challenges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class day10 {

    private static class Point {
        private int x;
        private int y;
        private int velocityX;
        private int velocityY;

        Point(int x, int y, int velocityX, int velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && getClass() == obj.getClass() && this.x == ((Point) obj).x && this.y == ((Point) obj).y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static Set<Point> getInitialPoints() {
        Set<Point> result = new HashSet<>();
        try {
            Files.lines(Paths.get("input/input_day10.txt"))
                    .forEach(fLine -> {
                        int x = Integer.parseInt(fLine.substring(fLine.indexOf("<") +1, fLine.indexOf(",")).trim());
                        int y = Integer.parseInt(fLine.substring(fLine.indexOf(",") +1, fLine.indexOf(">")).trim());
                        int velocityX = Integer.parseInt(fLine.substring(fLine.lastIndexOf("<") +1, fLine.lastIndexOf(",")).trim());
                        int velocityY = Integer.parseInt(fLine.substring(fLine.lastIndexOf(",") +1, fLine.lastIndexOf(">")).trim());
                        result.add(new Point(x, y, velocityX, velocityY));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Set<Point> getPointsAfterSec(Set<Point> points, int seconds) {
        Set<Point> newPositions = new HashSet<>();
        points.forEach(point -> {
            int newX = point.x + (point.velocityX * seconds);
            int newY = point.y + (point.velocityY * seconds);
            newPositions.add(new Point(newX, newY, point.velocityX, point.velocityY));
        });
        return newPositions;
    }

    private static void drawPoints(Set<Point> points) {
        AtomicInteger minX = new AtomicInteger(100000);
        AtomicInteger maxX = new AtomicInteger();
        AtomicInteger minY = new AtomicInteger(100000);
        AtomicInteger maxY = new AtomicInteger();
        points.forEach(point -> {
            if(point.x < minX.get()) minX.set(point.x);
            if(point.x > maxX.get()) maxX.set(point.x);
            if (point.y < minY.get()) minY.set(point.y);
            if(point.y > maxY.get()) maxY.set(point.y);
        });
        String[][] grid = new String[maxX.get() + Math.abs(minX.get()) + 1][maxY.get() + Math.abs(minY.get()) + 1];
        points.forEach(point -> grid[point.x + Math.abs(minX.get())][point.y + Math.abs(minY.get())] = "#");
        for (String[] gridX : grid) {
            for (String gridIn : gridX) {
                if (gridIn == null) {
                    System.out.print(".");
                } else {
                    System.out.print(gridIn);
                }
            }
            System.out.print("\n");
        }
    }

    private static boolean checkPointsAligned(Set<Point> points) {
        Iterator pointIt = points.iterator();
        Point point = (Point) pointIt.next();
        while(pointIt.hasNext()) {
            int x = point.x;
            int y = point.y;
            if(!points.contains(new Point(x - 1, y, point.velocityX, point.velocityY)) &&
               !points.contains(new Point(x, y - 1, point.velocityX, point.velocityY)) &&
               !points.contains(new Point(x - 1, y - 1, point.velocityX, point.velocityY)) &&
               !points.contains(new Point(x - 1, y + 1, point.velocityX, point.velocityY)) &&
               !points.contains(new Point(x + 1, y, point.velocityX, point.velocityY)) &&
               !points.contains(new Point(x, y + 1, point.velocityX, point.velocityY)) &&
               !points.contains(new Point(x + 1, y + 1, point.velocityX, point.velocityY)) &&
               !points.contains(new Point(x + 1, y - 1, point.velocityX, point.velocityY))) {
                return false;
            }
            point = (Point) pointIt.next();
        }
        return true;
    }

    public static void main(String[] args) {
        boolean aligned = false;
        int iteration = 0;
        Set<Point> initialPoints = getInitialPoints();
        while(!aligned) {
            Set<Point> pointsAfterSec = getPointsAfterSec(initialPoints, iteration);
            aligned = checkPointsAligned(pointsAfterSec);
            if(aligned) {
                System.out.println(iteration);
                drawPoints(pointsAfterSec);
            }
            iteration++;
        }
    }
}
