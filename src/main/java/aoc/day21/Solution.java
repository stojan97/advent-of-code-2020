package aoc.day21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day21");

    Foods foods = parseInput(lines);

    solve(foods);
  }

  private static void solve(Foods foods) {

    List<Set<String>> foodsList = foods.foodsList;
    Map<String, Map<String, Integer>> allergens = foods.res;
    List<ValidIngredient> validIngredients = new ArrayList<>();

    while(true) {
      ValidIngredient ingredient = foundValidIngredient(allergens);

      if (ingredient == null) {
        break;
      }

      validIngredients.add(ingredient);
      allergens.remove(ingredient.allergen);

      for (Map.Entry<String, Map<String, Integer>> allergen : allergens.entrySet()) {
        Map<String, Integer> ingredients = allergen.getValue();
        ingredients.remove(ingredient.ingredient);
      }

      for (Set<String> ingredientsSet : foodsList) {
        ingredientsSet.remove(ingredient.ingredient);
      }

    }

    int sum = foodsList.stream().mapToInt(Set::size).sum();
    StringBuilder res = new StringBuilder();
    List<ValidIngredient> sortedValidIngredients = validIngredients
      .stream()
      .sorted((i1, i2) -> i1.allergen.compareTo(i2.allergen))
      .collect(Collectors.toList());

    for (ValidIngredient sortedValidIngredient : sortedValidIngredients) {
      res.append(sortedValidIngredient.ingredient).append(",");
    }

    res.deleteCharAt(res.length() - 1);

    System.out.println("Part 1: " + sum);
    System.out.println("Part 2: " + res);
  }

  public static ValidIngredient foundValidIngredient(Map<String, Map<String, Integer>> allergens) {

    for (Map.Entry<String, Map<String, Integer>> allergen : allergens.entrySet()) {

      int maxValue = allergen.getValue().values().stream().mapToInt(i -> i).max().orElse(0);

      List<Map.Entry<String, Integer>> collect = allergen
        .getValue()
        .entrySet()
        .stream()
        .filter(entry -> maxValue == entry.getValue())
        .collect(Collectors.toList());

      if (collect.size() == 1) {
        return new ValidIngredient(allergen.getKey(), collect.get(0).getKey());
      }
    }

    return null;
  }

  private static Foods parseInput(List<String> lines) {

    Map<String, Map<String, Integer>> res = new HashMap<>();
    List<Set<String>> foods = new ArrayList<>();

    for (String line : lines) {

      Pattern pattern = Pattern.compile("\\(([^\\)]+)\\)");
      Matcher matcher = pattern.matcher(line);
      String allergensString = "";
      if (matcher.find()) {
        allergensString = matcher.group();
      }
      line = line.replace(allergensString, "");
      String[] s = line.split(" ");
      Set<String> ingredients = new HashSet<>(Arrays.asList(s));
      allergensString = allergensString
        .substring(1, allergensString.length() - 1)
        .replace("contains", "");

      String[] allergens = allergensString.split(", ");
      for (String allergen : allergens) {
        allergen = allergen.strip();
        Map<String, Integer> ing = res.getOrDefault(allergen, new HashMap<>());

        for (String ingredient : ingredients) {
          Integer count = ing.getOrDefault(ingredient, 0);
          ing.put(ingredient, count + 1);
        }

        res.put(allergen, ing);
      }

      foods.add(ingredients);
    }

    return new Foods(foods, res);
  }

  private static class Foods {

    private List<Set<String>> foodsList;
    private Map<String, Map<String, Integer>> res;

    public Foods(List<Set<String>> foodsList, Map<String, Map<String, Integer>> res) {

      this.foodsList = foodsList;
      this.res = res;
    }
  }

  private static class ValidIngredient {

    private final String allergen;
    private final String ingredient;

    public ValidIngredient(String allergen, String ingredient) {

      this.allergen = allergen;
      this.ingredient = ingredient;
    }
  }

}
