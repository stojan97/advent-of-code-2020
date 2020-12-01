package aoc.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  private static int getProductForTwoEntries(List<Integer> expenseReport) {

    Set<Integer> seen = new HashSet<>();

    for (int i : expenseReport) {
      int diff = 2020 - i;
      if (seen.contains(diff)) {
        return diff * i;
      }
      seen.add(i);
    }

    return 0;
  }

  private static int getProductForThreeEntries(List<Integer> expenseReport) {

    for (int i = 0; i < expenseReport.size(); i++) {
      for (int j = i; j < expenseReport.size(); j++) {
        for (int k = j; k < expenseReport.size(); k++) {
          int first = expenseReport.get(i);
          int second = expenseReport.get(j);
          int third = expenseReport.get(k);
          if (first + second + third == 2020) {
            return first * second * third;
          }
        }
      }
    }

    return 0;
  }

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("aoc/day1/input.txt");
    List<Integer> expenseReport = lines.stream().map(Integer::parseInt).collect(Collectors.toList());

    int productForTwo = getProductForTwoEntries(expenseReport);
    int productForThree = getProductForThreeEntries(expenseReport);

    System.out.println("Part 1: " + productForTwo);
    System.out.println("Part 2: " + productForThree);
  }
}
