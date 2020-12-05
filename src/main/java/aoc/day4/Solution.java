package aoc.day4;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  private static final List<String> SUPPORTED_EYE_COLORS = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("aoc/day4/input.txt");

    int validPassports = getValidPassports(lines, Solution::validateFields);

    int validPassportsAllChecks = getValidPassports(lines, Solution::validateFields, Solution::validateValues);

    System.out.println("Part 1: " + validPassports);
    System.out.println("Part 2: " + validPassportsAllChecks);
  }

  private static Map<String, String> extractFields(String line) {

    if (line.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<String, String> fields = new HashMap<>();
    String[] pairs = line.split(" ");

    for (String pair : pairs) {
      String[] field = pair.split(":");
      fields.put(field[0], field[1]);
    }

    return fields;
  }

  private static int getValidPassports(List<String> lines, BooleanSupplier... booleanSuppliers) {

    Map<String, String> seenFields = new HashMap<>();
    int count = 0;

    for (int i = 0; i < lines.size(); i++) {

      String line = lines.get(i);
      seenFields.putAll(extractFields(line));

      if (line.isEmpty() || i == lines.size() - 1) {

        if (checkBooleans(seenFields, booleanSuppliers)) {
          count++;
        }
        seenFields.clear();
      }
    }

    return count;
  }

  private static boolean checkBooleans(Map<String, String> seenFields, BooleanSupplier[] booleanSuppliers) {

    boolean result = true;

    for (BooleanSupplier booleanSupplier : booleanSuppliers) {
      result &= booleanSupplier.getBoolean(seenFields);
    }

    return result;
  }

  private static boolean validateFields(Map<String, String> seenFields) {

    return seenFields.size() == 8 || (seenFields.size() == 7 && !seenFields.containsKey("cid"));
  }

  private static boolean valueInRange(int value, int low, int high) {
    return value >= low && value <= high;
  }

  private static boolean validateValues(Map<String, String> seenFields) {

    boolean result;
    // byr
    int byr = Integer.parseInt(seenFields.getOrDefault("byr", "0"));
    result = valueInRange(byr, 1920, 2002);
    // iyr
    int iyr = Integer.parseInt(seenFields.getOrDefault("iyr", "0"));
    result &= valueInRange(iyr, 2010, 2020);
    // eyr
    int eyr = Integer.parseInt(seenFields.getOrDefault("eyr", "0"));
    result &= valueInRange(eyr, 2020, 2030);
    // hgt
    String hgt = seenFields.getOrDefault("hgt", "0");

    if (hgt.contains("cm")) {
      result &= valueInRange(Integer.parseInt(hgt.split("cm")[0]), 150, 193);
    } else if (hgt.contains("in")) {
      result &= valueInRange(Integer.parseInt(hgt.split("in")[0]), 59, 76);
    } else {
      return false;
    }
    // hcl
    String hcl = seenFields.getOrDefault("hcl", "0");

    if (hcl.length() != 7 || !hcl.startsWith("#")) {
      return false;
    }

    for (Character character : hcl.substring(1).toCharArray()) {
      result &= Character.isDigit(character) || (character >= 'a' && character <= 'f');
    }

    result &= valueInRange(eyr, 2020, 2030);

    // ecl
    String ecl = seenFields.getOrDefault("ecl", "0");
    result &= SUPPORTED_EYE_COLORS.contains(ecl);

    // pid
    String pid = seenFields.getOrDefault("pid", "0");

    if (pid.length() != 9) {
      return false;
    }

    for (Character digit: pid.toCharArray()) {
      result &= Character.isDigit(digit);
    }

    return result;
  }

  @FunctionalInterface
  private interface BooleanSupplier {

    boolean getBoolean(Map<String, String> seenFields);

  }
}
