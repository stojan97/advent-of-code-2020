package aoc.day14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day14");

    long sumForValue = sumValuesInMemoryForValue(lines);
    long sumForAddress = sumValuesInMemoryForAddress(lines);

    System.out.println("Part 1: " + sumForValue);
    System.out.println("Part 2: " + sumForAddress);
  }

  private static long sumValuesInMemoryForValue(List<String> lines) {

    long sum = 0L;
    Map<String, Long> memory = new HashMap<>();
    String mask = "";
    for (String line : lines) {

      String[] s = line.split(" ");

      if (s[0].equals("mask")) {
        // mask is 35 bit binary number
        mask = s[2];
        continue;
      }

      char[] reversedMask = new StringBuilder(mask).reverse().toString().toCharArray();
      long current = Long.parseLong(s[2]);
      for (int i = 0; i < reversedMask.length; i++) {
        char c = reversedMask[i];

        if (c == '1') {
          current |= 1L << i;
        } else if (c == '0') {
          current &= ~(1L << i);
        }
      }

      memory.put(s[0], current);
    }

    for (Map.Entry<String, Long> entry: memory.entrySet()) {
      sum += entry.getValue();
    }

    return sum;
  }

  private static long sumValuesInMemoryForAddress(List<String> lines) {

    long sum = 0L;
    Map<Long, Long> memory = new HashMap<>();
    String mask = "";
    for (String line : lines) {

      String[] s = line.split(" ");

      if (s[0].equals("mask")) {
        // mask is 35 bit binary number
        mask = s[2];
        continue;
      }

      String reversedString = new StringBuilder(mask).reverse().toString();
      char[] reversedMask = reversedString.toCharArray();
      String[] firstSplit = s[0].split("mem\\[");
      String[] secondSplit = firstSplit[1].split("]");

      long address = Long.parseLong(secondSplit[0]);
      long current = Long.parseLong(s[2]);

      List<Integer> floatingBits = new ArrayList<>();

      for (int i = 0; i < reversedMask.length; i++) {
        char c = reversedMask[i];

        if (c == '1') {
          address |= 1L << i;
        } else if (c == 'X') {
          floatingBits.add(i);
        }
      }

      if (floatingBits.isEmpty()) {

        memory.put(address, current);
      } else {

        traverseFloatingBits(0, floatingBits, memory, address, current);
      }

    }

    for (Map.Entry<Long, Long> entry: memory.entrySet()) {
      sum += entry.getValue();
    }

    return sum;
  }

  private static void traverseFloatingBits(
    int index,
    List<Integer> floatingBits,
    Map<Long, Long> memory,
    long address,
    long current) {

    if (index > floatingBits.size() - 1) {
      memory.put(address, current);
      return;
    }

    int currentFloatingBit = floatingBits.get(index);

    long newAddressWithSetBit = address | (1L << currentFloatingBit);
    long newAddressWithClearedBit = address & ~(1L << currentFloatingBit);

    traverseFloatingBits(index + 1, floatingBits, memory, newAddressWithSetBit, current);
    traverseFloatingBits(index + 1, floatingBits, memory, newAddressWithClearedBit, current);
  }

}
