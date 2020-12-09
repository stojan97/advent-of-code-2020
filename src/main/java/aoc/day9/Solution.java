package aoc.day9;

import java.util.List;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  private static final int PREAMBLE = 25;

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day9");
    List<Long> numbers = lines.stream().map(Long::parseLong).collect(Collectors.toList());
    long firstInvalidNumber = getFirstInvalidNumber(numbers);
    long encryptionWeakness = getEncryptionWeakness(numbers, firstInvalidNumber);

    System.out.println("Part 1: " + firstInvalidNumber);
    System.out.println("Part 2: " + encryptionWeakness);
  }

  private static long getEncryptionWeakness(List<Long> numbers, long firstInvalidNumber) {

    for (int i = 0; i < numbers.size(); i++) {
      Long current = numbers.get(i);

      long sum = current;
      long smallest = current;
      long largest = current;
      if (current == firstInvalidNumber) {
        continue;
      }
      for (int j = i + 1; j < numbers.size(); j++) {
        Long other = numbers.get(j);
        sum += other;
        smallest = Math.min(smallest, other);
        largest = Math.max(largest, other);

        if (sum >= firstInvalidNumber) {
          break;
        }
      }

      if (sum == firstInvalidNumber) {
        return smallest + largest;
      }
    }

    return -1;
  }

  private static long getFirstInvalidNumber(List<Long> numbers) {

    for (int i = PREAMBLE; i < numbers.size(); i++) {
      boolean found = false;
      for (int j = i - PREAMBLE; j < i; j++) {
        for (int k = j + 1; k < i; k++) {
          if (numbers.get(j) + numbers.get(k) == numbers.get(i)) {
            found = true;
            break;
          }
        }
        if (found) {
          break;
        }
      }
      if (!found) {
        return numbers.get(i);
      }
    }

    return -1;
  }
}
