package aoc.day16;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<List<String>> groups = FileReader.readLinesByGroups("day16");

    List<Rule> rules = parseRules(groups);
    List<Integer> myTicketValues = parseMyTicket(groups);
    List<List<Integer>> nearbyTickets = parseNearbyTickets(groups);

    int ticketScanningErrorRate = getTicketScanningErrorRate(rules, nearbyTickets);
    long departureValuesMultiplied = getDepartureValuesMultiplied(rules, nearbyTickets, myTicketValues);
    System.out.println("Part 1: " + ticketScanningErrorRate);
    System.out.println("Part 2: " + departureValuesMultiplied);
  }

  private static long getDepartureValuesMultiplied(
    List<Rule> rules,
    List<List<Integer>> nearbyTickets,
    List<Integer> myTicketValues) {

    List<List<Integer>> validNearbyTickets = getValidNearbyTickets(rules, nearbyTickets);
    List<Field> fields = new ArrayList<>();

    for (Rule rule : rules) {

      Set<Integer> positions = getPositions(validNearbyTickets, rule);
      fields.add(new Field(rule.ticketFieldName, positions));
    }

    fields.sort((field1, field2) -> Integer.compare(field1.positions.size(), field2.positions.size()));
    long res = 1L;
    Set<Integer> seen = new HashSet<>();

    for (Field field : fields) {

      field.positions.removeAll(seen);

      if (field.positions.size() != 1) {
        String msg = "Field positions should contain only one element";
        throw new RuntimeException(msg);
      }
      List<Integer> resList = new ArrayList<>(field.positions);
      int currentValue = resList.get(0);

      if (field.fieldName.contains("departure")) {
        res *= myTicketValues.get(currentValue);
      }

      seen.add(currentValue);
    }

    return res;
  }

  private static Set<Integer> getPositions(
    List<List<Integer>> validNearbyTickets,
    Rule rule) {

    Set<Integer> positions = new HashSet<>();

    int size = validNearbyTickets.get(0).size();

    for (int j = 0; j < size; j++) {
      boolean isValid = true;

      for (List<Integer> validNearbyTicket : validNearbyTickets) {
        Integer value = validNearbyTicket.get(j);
        isValid &= rule.isValueInRange(value);
      }

      if (isValid) {
        positions.add(j);
      }
    }

    return positions;
  }

  private static List<List<Integer>> getValidNearbyTickets(List<Rule> rules, List<List<Integer>> nearbyTickets) {

    List<List<Integer>> validNearbyTickets = new ArrayList<>();

    for (List<Integer> nearbyTicket : nearbyTickets) {

      boolean isValidTicket = true;

      for (int value : nearbyTicket) {

        boolean isValid = false;

        for (Rule rule : rules) {
          isValid |= rule.isValueInRange(value);
        }

        isValidTicket &= isValid;

      }

      if (isValidTicket) {
        validNearbyTickets.add(nearbyTicket);
      }

    }

    return validNearbyTickets;
  }

  private static int getTicketScanningErrorRate(List<Rule> rules, List<List<Integer>> nearbyTickets) {

    return nearbyTickets
      .stream()
      .mapToInt(nearbyTicket -> getCurrentTicketErrorRate(rules, nearbyTicket))
      .sum();
  }

  private static int getCurrentTicketErrorRate(List<Rule> rules, List<Integer> nearbyTicket) {

    int currentTicketErrorRate = 0;

    for (int value : nearbyTicket) {
      boolean isValid = false;

      for (Rule rule : rules) {
        isValid |= rule.isValueInRange(value);
      }

      if (!isValid) {
        currentTicketErrorRate += value;
      }
    }

    return currentTicketErrorRate;
  }

  private static List<List<Integer>> parseNearbyTickets(List<List<String>> groups) {

    List<List<Integer>> nearbyTicketValues = new ArrayList<>();

    List<String> values = groups.get(2);

    for (int i = 1; i < values.size(); i++) {
      String[] separatedValues = values.get(i).split(",");
      List<Integer> list = new ArrayList<>();
      for (String value : separatedValues) {
        list.add(Integer.parseInt(value));
      }
      nearbyTicketValues.add(list);
    }

    return nearbyTicketValues;
  }

  private static List<Integer> parseMyTicket(List<List<String>> groups) {

    List<Integer> myTicketValues = new ArrayList<>();
    String[] s = groups.get(1).get(1).split(",");

    for (String value : s) {
      myTicketValues.add(Integer.parseInt(value));
    }

    return myTicketValues;
  }

  private static List<Rule> parseRules(List<List<String>> groups) {

    List<Rule> rules = new ArrayList<>();

    for (String line : groups.get(0)) {
      String[] s = line.split(":");
      String ticketFieldName = s[0];
      String regex = " or ";
      String[] secondSplit = s[1].strip().split(regex);
      String[] firstRange = secondSplit[0].split("-");
      String[] secondRange = secondSplit[1].split("-");

      rules.add(new Rule(
          ticketFieldName,
          Integer.parseInt(firstRange[0]),
          Integer.parseInt(firstRange[1]),
          Integer.parseInt(secondRange[0]),
          Integer.parseInt(secondRange[1])));
    }

    return rules;
  }

  private static class Rule {

    String ticketFieldName;
    int firstRangeStart;
    int firstRangeEnd;
    int secondRangeStart;
    int secondRangeEnd;

    public Rule(
      String ticketFieldName,
      int firstRangeStart,
      int firstRangeEnd,
      int secondRangeStart,
      int secondRangeEnd) {

      this.ticketFieldName = ticketFieldName;
      this.firstRangeStart = firstRangeStart;
      this.firstRangeEnd = firstRangeEnd;
      this.secondRangeStart = secondRangeStart;
      this.secondRangeEnd = secondRangeEnd;
    }

    private boolean isValueInRange(int value) {

      return (value >= firstRangeStart && value <= firstRangeEnd)
             || (value >= secondRangeStart && value <= secondRangeEnd);
    }
  }

  private static class Field {

    String fieldName;
    Set<Integer> positions;

    public Field(String fieldName, Set<Integer> positions) {

      this.fieldName = fieldName;
      this.positions = positions;
    }
  }

}
