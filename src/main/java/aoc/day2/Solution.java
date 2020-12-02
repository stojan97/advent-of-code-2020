package aoc.day2;

import java.util.List;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  private static int getValidPasswords(List<String> lines, BooleanSupplier booleanSupplier) {

    int validPasswords = 0;

    for (String line : lines) {

      String[] s = line.split(" ");
      String[] numbers = s[0].split("-");
      char letter = s[1].split(":")[0].charAt(0);
      List<Character> chars = s[2].chars().mapToObj(c -> (char) c).collect(Collectors.toList());
      long count = chars.stream().filter(c -> c == letter).count();
      int first = Integer.parseInt(numbers[0]);
      int second = Integer.parseInt(numbers[1]);

      if (booleanSupplier.getBoolean(first, second, chars, letter, count)) {
        validPasswords++;
      }
    }

    return validPasswords;
  }

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("aoc/day2/input.txt");

    int validPasswordsByCount = getValidPasswords(
      lines,
      (first, second, chars, letter, count) -> first <= count && second >= count);

    int validPasswordsByPositions = getValidPasswords(
      lines,
      (first, second, chars, letter, count) -> chars.get(first - 1) == letter ^ chars.get(second - 1) == letter);

    System.out.println("Part 1: " + validPasswordsByCount);
    System.out.println("Part 2: " + validPasswordsByPositions);
  }

  @FunctionalInterface
  private interface BooleanSupplier {

    boolean getBoolean(int first, int second, List<Character> chars, char letter, long count);
  }

}
