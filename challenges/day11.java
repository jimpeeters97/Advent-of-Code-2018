package challenges;

public class day11 {

    private static int[][] fillGrid() {
        int gridSerial = 6042;
        int[][] grid = new int[300][300];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                int rackId = x + 11;
                String subResult = String.valueOf((rackId * (y + 1) + gridSerial) * rackId);
                grid[x][y] = Integer.parseInt(subResult.substring(subResult.length() - 3, subResult.length() - 2)) - 5;
            }
        }
        return grid;
    }

    private static String getTopLeftCellHighestFuelSpace(int[][] grid) {
        int highestLevel = 0;
        String topLeftCell = "";
        for (int size = 1; size < grid.length; size++) {
            for (int x = 0; x < grid.length - size; x++) {
                for (int y = 0; y < grid[x].length - size; y++) {
                    int combinedFuelScore = 0;
                    for (int z = 0; z <= size; z++) {
                        for (int a = 0; a <= size; a++) {
                            combinedFuelScore += grid[x + z][y + a];
                        }
                    }
                    if(combinedFuelScore > highestLevel) {
                        highestLevel = combinedFuelScore;
                        topLeftCell = String.format("%s,%s,%s", x + 1, y + 1, size + 1);
                    }
                }
            }
        }
        return topLeftCell;
    }

    public static void main(String[] args) {
        System.out.println(getTopLeftCellHighestFuelSpace(fillGrid()));
    }
}
