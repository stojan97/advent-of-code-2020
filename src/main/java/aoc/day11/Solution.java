package aoc.day11;

import aoc.utils.FileReader;

public class Solution {

  private static final int[] DX = { -1, -1, -1, 0, 0, 1, 1, 1 };
  private static final int[] DY = { -1, 0, 1, -1, 1, -1, 0, 1 };

  public static void main(String[] args) {

    char[][] map = FileReader.readLinesAs2dMap("day11");

    int countOccupiedFirstPart = getCountOccupied(map, 1, 4);
    int countOccupiedSecondPart = getCountOccupied(map, -1, 5);

    System.out.println("Part 1: " + countOccupiedFirstPart);
    System.out.println("Part 2: " + countOccupiedSecondPart);
  }

  /**
   * Gets the count of occupied seats after no state change.
   *
   * @param map the given input map
   * @param depthPerDirection -1 (until seat found) or 1 (one iteration)
   * @param occupiedSeats visible occupied seats for an occupied seat to become empty
   *
   * @return the count of occupied seats after no state change
   */
  private static int getCountOccupied(char[][] map, int depthPerDirection, int occupiedSeats) {

    int count = 0;
    boolean stateChanged = false;
    char[][] changingMap = new char[map.length][map[0].length];

    for (int i = 0; i < changingMap.length; i++) {
      System.arraycopy(map[i], 0, changingMap[i], 0, changingMap[0].length);
    }

    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {

        int occupied = 0;
        for (int k = 0; k < 8; k++) {
          int occupiedSeatsInDirection = occupiedSeatsInDirection(map, i, j, k, depthPerDirection);
          occupied += occupiedSeatsInDirection;
        }

        if (map[i][j] == 'L' && occupied == 0) {
          changingMap[i][j] = '#';
          stateChanged = true;
        }

        if (map[i][j] == '#' && occupied >= occupiedSeats) {
          changingMap[i][j] = 'L';
          stateChanged = true;
        }

        if (changingMap[i][j] == '#') {
          count++;
        }
      }
    }

    if (stateChanged) {
      return getCountOccupied(changingMap, depthPerDirection, occupiedSeats);
    }

    return count;
  }

  private static int occupiedSeatsInDirection(char[][] map, int i, int j, int k, int depth) {

    int dx = i + DX[k];
    int dy = j + DY[k];

    if (depth == 1) {
      if (inRange(map, dx, dy) && map[dx][dy] == '#') {
        return 1;
      }
      return 0;
    }

    while (inRange(map, dx, dy) && map[dx][dy] == '.') {
      dx += DX[k];
      dy += DY[k];
    }

    char firstSeat = inRange(map, dx, dy) ? map[dx][dy] : '.';

    return firstSeat == '#' ? 1 : 0;
  }

  private static boolean inRange(char[][] map, int dx, int dy) {

    return dx >= 0 && dx < map.length && dy >= 0 && dy < map[0].length;
  }
}
