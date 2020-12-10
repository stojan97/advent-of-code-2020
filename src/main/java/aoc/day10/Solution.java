package aoc.day10;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day10");
    List<Integer> adapters = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
    adapters.add(0);
    Collections.sort(adapters);
    adapters.add(adapters.get(adapters.size() - 1) + 3);

    int multipliedDifferences = getMultipliedDifferences(adapters);
    long numberOfArrangements = getNumberOfArrangements(adapters);

    System.out.println("Part 1: " + multipliedDifferences);
    System.out.println("Part 2: " + numberOfArrangements);
  }

  private static int getMultipliedDifferences(List<Integer> adapters) {
    int oneJolts = 0;
    int threeJolts = 0;

    for (int i = 1; i < adapters.size(); i++) {
      int diff = adapters.get(i) - adapters.get(i - 1);

      if (diff == 1) {
        oneJolts++;
      }

      if (diff == 3) {
        threeJolts++;
      }
    }

    return oneJolts * threeJolts;
  }

  private static long getNumberOfArrangements(List<Integer> adapters) {

    Map<Integer, Long> adaptersMap = adapters.stream().collect(Collectors.toMap(Integer::intValue, integer -> 0L));
    adaptersMap.put(0, 1L);

    for (int i = 1; i < adapters.size(); i++) {
      int current = adapters.get(i);
      long one = adaptersMap.getOrDefault(current - 1, 0L);
      long two = adaptersMap.getOrDefault(current - 2, 0L);
      long three = adaptersMap.getOrDefault(current - 3, 0L);

      adaptersMap.put(current, one + two + three);
    }

    return adaptersMap.get(adapters.get(adapters.size() - 1));
  }
}
