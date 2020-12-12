package aoc.day12;

import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  private static final Map<Integer, Integer> DIR_X = Map.of(2, 0, 0, 0, 1, -1, 3, 1);

  private static final Map<Integer, Integer> DIR_Y = Map.of(2, -1, 0, 1, 1, 0, 3, 0);

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day12");

    int distanceForShip = getDistanceForShip(lines);
    int distanceForShipWithWaypoint = getDistanceForShipWithWaypoint(lines);

    System.out.println("Part 1: " + distanceForShip);
    System.out.println("Part 2: " + distanceForShipWithWaypoint);
  }

  private static int getDistanceForShip(List<String> lines) {

    int startX = 0;
    int startY = 0;

    int dir = 0;

    for (String line : lines) {
      String action = line.substring(0, 1);
      int value = Integer.parseInt(line.substring(1));

      if (action.equals("N")) {
        startX -= value;
      }

      if (action.equals("S")) {
        startX += value;
      }

      if (action.equals("E")) {
        startY += value;
      }

      if (action.equals("W")) {
        startY -= value;
      }

      if (action.equals("F")) {
        startX += value * DIR_X.get(dir);
        startY += value * DIR_Y.get(dir);
      }

      if (action.equals("L")) {
        dir = (dir + value / 90) % 4;
      }

      if (action.equals("R")) {
        dir -= value / 90;
        if (dir < 0) {
          dir = 4 + dir;
        }
      }
    }

    return Math.abs(startX) + Math.abs(startY);
  }

  private static int getDistanceForShipWithWaypoint(List<String> lines) {

    int startX = 0;
    int startY = 0;
    int wayPointX = -1;
    int wayPointY = 10;

    for (String line : lines) {
      String action = line.substring(0, 1);
      int value = Integer.parseInt(line.substring(1));

      if (action.equals("N")) {
        wayPointX -= value;
      }

      if (action.equals("S")) {
        wayPointX += value;
      }

      if (action.equals("E")) {
        wayPointY += value;
      }

      if (action.equals("W")) {
        wayPointY -= value;
      }

      if (action.equals("F")) {
        startX += value * wayPointX;
        startY += value * wayPointY;
      }

      if (action.equals("L")) {
        int times = value / 90;
        while (times > 0) {
          int temp = wayPointX;
          wayPointX = -wayPointY;
          wayPointY = temp;
          times--;
        }
      }

      if (action.equals("R")) {
        int times = value / 90;
        while (times > 0) {
          int temp = wayPointX;
          wayPointX = wayPointY;
          wayPointY = -temp;
          times--;
        }
      }
    }

    return Math.abs(startX) + Math.abs(startY);
  }

}
