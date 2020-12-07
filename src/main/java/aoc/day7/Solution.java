package aoc.day7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  private static final String SHINY_GOLD = "shiny_gold";

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day7");

    Map<String, List<Bag>> rules = parseRules(lines);

    int outerBags = getNumberOfOuterBagsForShinyGold(rules);
    int bagsInsideSingleShinyGold = getBagsCount(SHINY_GOLD, rules);
    System.out.println("Part 1: " + outerBags);
    System.out.println("Part 2: " + bagsInsideSingleShinyGold);
  }

  private static int getBagsCount(String currentBag, Map<String, List<Bag>> rules) {
    
    int count = 0;

    List<Bag> bags = rules.get(currentBag);

    for (Bag bag : bags) {
      count += bag.quantity + bag.quantity * getBagsCount(bag.color, rules);
    }

    return count;
  }

  private static int getNumberOfOuterBagsForShinyGold(Map<String, List<Bag>> rules) {

    int count = 0;
    for (String bag : rules.keySet()) {
      if (!bag.equals(SHINY_GOLD) && traverseToFindOuterMostBag(bag, rules)) {
        count++;
      }
    }

    return count;
  }

  private static boolean traverseToFindOuterMostBag(String currentBag, Map<String, List<Bag>> rules) {

    if (currentBag.equals(SHINY_GOLD)) {
      return true;
    }

    boolean res = false;

    List<Bag> bags = rules.get(currentBag);

    for (Bag bag : bags) {
      res |= traverseToFindOuterMostBag(bag.color, rules);
    }

    return res;
  }

  private static Map<String, List<Bag>> parseRules(List<String> lines) {

    Map<String, List<Bag>> rules = new HashMap<>();

    for (String line : lines) {
      String[] splits = line.split(" ");
      String color = String.format("%s_%s", splits[0], splits[1]);
      List<Bag> contents = new ArrayList<>();
      if (!splits[4].equals("no")) {
        for (int i = 4; i < splits.length; i += 4) {
          int quantity = Integer.parseInt(splits[i]);
          String currentColor = String.format("%s_%s", splits[i + 1], splits[i + 2]);
          Bag bag = new Bag(quantity, currentColor);
          contents.add(bag);
        }
      }
      rules.put(color, contents);
    }

    return rules;
  }

  private static class Bag {

    int quantity;
    String color;

    @Override
    public String toString() {

      return "Bag{" + "quantity=" + quantity + ", color='" + color + '\'' + '}';
    }

    public Bag(int quantity, String color) {

      this.quantity = quantity;
      this.color = color;
    }
  }

}
