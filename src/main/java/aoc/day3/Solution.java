package aoc.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    char[][] treeMap = FileReader.readLinesAs2dMap("day3");

    List<Slope> slopes = new ArrayList<>();
    slopes.add(new Slope(1, 1));
    slopes.add(new Slope(1, 3));
    slopes.add(new Slope(1, 5));
    slopes.add(new Slope(1, 7));
    slopes.add(new Slope(2, 1));

    calculateTreesCountForAllSlopes(slopes, treeMap);

    int firstPartTreesCount = slopes.get(1).count;
    int productOfSlopesCount = multiplyTreesCountForAllSlopes(slopes);

    System.out.println("Part 1: " + firstPartTreesCount);
    System.out.println("Part 2: " + productOfSlopesCount);
  }

  private static void calculateTreesCountForAllSlopes(List<Slope> slopes, char[][] treeMap) {

    int rowSize = treeMap.length;

    while (notFinished(slopes, rowSize)) {

      countTreesForNotFinishedSlopes(slopes, treeMap);
      traverseMapForNotFinishedSlopes(slopes, treeMap);
    }
  }

  private static int multiplyTreesCountForAllSlopes(List<Slope> slopes) {

    return slopes
      .stream()
      .map(Slope::getCount)
      .reduce(1, Math::multiplyExact);
  }

  private static void traverseMapForNotFinishedSlopes(List<Slope> slopes, char[][] treeMap) {

    List<Slope> notFinishedSlopes = getNotFinishedSlopes(slopes, treeMap.length);
    notFinishedSlopes.forEach(slope -> slope.traverseMap(treeMap[0].length));
  }

  private static void countTreesForNotFinishedSlopes(List<Slope> slopes, char[][] treeMap) {

    List<Slope> notFinishedSlopes = getNotFinishedSlopes(slopes, treeMap.length);
    notFinishedSlopes.forEach(slope -> slope.countTreeForSlope(treeMap));
  }

  private static boolean notFinished(List<Slope> slopes, int rowSize) {

    return !getNotFinishedSlopes(slopes, rowSize).isEmpty();
  }

  private static List<Slope> getNotFinishedSlopes(List<Slope> slopes, int rowSize) {

    return slopes
      .stream()
      .filter(slope -> slope.currentRow < rowSize)
      .collect(Collectors.toList());
  }

  private static class Slope {
    public int di;
    public int dj;
    public int currentRow = 0;
    public int currentCol = 0;

    public int count = 0;

    public int getCount() {

      return count;
    }

    private Slope(int di, int dj) {

      this.di = di;
      this.dj = dj;
    }

    public void countTreeForSlope(char[][] treeMap) {

      if (treeMap[currentRow][currentCol] == '#') {
        count++;
      }
    }

    public void traverseMap(int colSize) {

      currentRow += di;
      currentCol = (currentCol + dj) % colSize;
    }

  }
}
