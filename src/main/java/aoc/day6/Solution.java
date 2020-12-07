package aoc.day6;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<List<String>> lines = FileReader.readLinesByGroups("day6");

    int count = getCounts(lines);
    int countsAnsweredByEveryone = getCountsAnsweredByEveryone(lines);
    System.out.println("Part 1: " + count);
    System.out.println("Part 2: " + countsAnsweredByEveryone);
  }

  private static int getCounts(List<List<String>> lines) {

    int count = 0;

    for (List<String> group : lines) {
      Set<Character> seenFields = new HashSet<>();

      for (String line : group) {
        for (char c : line.toCharArray()) {
          seenFields.add(c);
        }
      }
      count += seenFields.size();
    }

    return count;
  }

  private static int getCountsAnsweredByEveryone(List<List<String>> lines) {

    int count = 0;

    for (List<String> group : lines) {
      Map<Character, Integer> seenFields = new HashMap<>();

      for (String line : group) {
        for (char c : line.toCharArray()) {
          seenFields.put(c, seenFields.getOrDefault(c, 0) + 1);
        }
      }

      for (Map.Entry<Character, Integer> entry : seenFields.entrySet()) {
        if (entry.getValue() == group.size()) {
          count++;
        }
      }

    }

    return count;
  }

}
