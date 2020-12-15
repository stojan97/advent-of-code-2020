package aoc.day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day15");
    String[] split = lines.get(0).split(",");
    List<Integer> startingNumbers = new ArrayList<>();
    for (String s : split) {
      startingNumbers.add(Integer.parseInt(s));
    }

    int numberSpokenFirstPart = getNumberSpoken(startingNumbers, 2020);
    // Runs for 4 seconds which is fine for solving part 2.
    int numberSpokenSecondPart = getNumberSpoken(startingNumbers, 30000000);
    System.out.println("Part 1: " + numberSpokenFirstPart);
    System.out.println("Part 2: " + numberSpokenSecondPart);
  }

  private static int getNumberSpoken(List<Integer> startingNumbers, int n) {

    int turn = 0;
    Map<Integer, Range> lastNumbersSpoken = new HashMap<>();
    for (; turn < startingNumbers.size(); turn++) {
      lastNumbersSpoken.put(startingNumbers.get(turn), new Range(-1, turn + 1));
    }
    int lastNumber = startingNumbers.get(startingNumbers.size() - 1);
    turn++;

    for (; turn <= n; turn++) {

      if (lastNumbersSpoken.containsKey(lastNumber) && lastNumbersSpoken.get(lastNumber).prevSpoken != -1) {

        Range range = lastNumbersSpoken.get(lastNumber);
        lastNumber = range.currentSpoken - range.prevSpoken;

        if (lastNumbersSpoken.containsKey(lastNumber)) {
          range = lastNumbersSpoken.get(lastNumber);
          range.prevSpoken = range.currentSpoken;
          range.currentSpoken = turn;
          lastNumbersSpoken.put(lastNumber, range);
        } else {
          lastNumbersSpoken.put(lastNumber, new Range(-1, turn));
        }

      } else {

        if (lastNumbersSpoken.containsKey(0)) {

          Range range = lastNumbersSpoken.get(0);
          range.prevSpoken = range.currentSpoken;
          range.currentSpoken = turn;
          lastNumbersSpoken.put(0, range);
        } else {
          lastNumbersSpoken.put(0, new Range(turn, turn));
        }

        lastNumber = 0;
      }

    }

    return lastNumber;
  }

  static class Range {

    int prevSpoken;
    int currentSpoken;

    public Range(int prevSpoken, int currentSpoken) {

      this.prevSpoken = prevSpoken;
      this.currentSpoken = currentSpoken;
    }
  }

}
