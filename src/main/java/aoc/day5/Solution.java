package aoc.day5;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("aoc/day5/input.txt");

    List<Integer> allSeats = getAllSeats(lines);
    int highestSeatOnTheBoardingPass = getHighestSeat(allSeats);
    int findMissingSeatId = findMissingSeatId(allSeats);

    System.out.println("Part 1: " + highestSeatOnTheBoardingPass);
    System.out.println("Part 2: " + findMissingSeatId);
  }

  private static int getHighestSeat(List<Integer> allSeats) {

    return allSeats
      .stream()
      .max(Comparator.naturalOrder())
      .orElse(Integer.MIN_VALUE);
  }

  private static int findMissingSeatId(List<Integer> allSeats) {

    int lowestSeatId = allSeats.get(0);

    for (int i : allSeats) {
      if (i != lowestSeatId) {
        return lowestSeatId;
      }
      lowestSeatId++;
    }
    return -1;
  }

  private static List<Integer> getAllSeats(List<String> lines) {

    return lines
      .stream()
      .map(Solution::getCalculatedSeatId)
      .sorted()
      .collect(Collectors.toList());
  }

  private static int getCalculatedSeatId(String line) {

    String rowsSeat = line.substring(0, 7);
    String columnsSeat = line.substring(7);

    int row = calculateForSeats(rowsSeat, 'F', 128);
    int column = calculateForSeats(columnsSeat, 'L', 8);

    return row * 8 + column;
  }

  private static int calculateForSeats(String seat, char lowChar, int high) {

    int low = 0;
    int index = 0;

    while (high - low != 2) {

      int mid = (low + high) / 2;

      if (seat.charAt(index) == lowChar) {
        high = mid;
      } else {
        low = mid;
      }
      index++;
    }

    return (seat.charAt(index) == lowChar) ? low : high - 1;
  }

}
