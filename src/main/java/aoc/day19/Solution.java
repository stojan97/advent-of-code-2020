package aoc.day19;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<List<String>> groups = FileReader.readLinesByGroups("day19");

    Map<String, Rule> rules = parseRules(groups);
    List<String> words = groups.get(1);
    int howManyMatchesWithoutLoops = getHowManyMatchesWithoutLoops(rules, words);
    int howManyMatchesWithLoops = getHowManyMatchesWithLoops(rules, words);

    System.out.println("Part 1: " + howManyMatchesWithoutLoops);
    System.out.println("Part 2: " + howManyMatchesWithLoops);
  }

  private static int getHowManyMatchesWithLoops(Map<String, Rule> rules, List<String> words) {

    int matching = 0;
    List<String> possibleMatchesFortyTwo = getPossibleMatches("42", rules);
    Set<String> possibleMatchesFortyTwoSet = new HashSet<>(possibleMatchesFortyTwo);
    List<String> possibleMatchesThirtyOne = getPossibleMatches("31", rules);
    Set<String> possibleMatchesThirtyOneSet = new HashSet<>(possibleMatchesThirtyOne);

    int fortyTwoWordsLen = possibleMatchesFortyTwo.get(0).length();
    int thirtyOneWordsLen = possibleMatchesThirtyOne.get(0).length();

    for (String word : words) {

      boolean isMatch = true;
      List<String> sequence = new ArrayList<>();

      while (!word.isEmpty()) {

        String currentWordFortyTwo = word.substring(0, fortyTwoWordsLen);
        String currentWordThirtyOne = word.substring(0, thirtyOneWordsLen);

        if (possibleMatchesFortyTwoSet.contains(currentWordFortyTwo)) {
          word = word.substring(fortyTwoWordsLen);
          sequence.add("42");
        } else if (possibleMatchesThirtyOneSet.contains(currentWordThirtyOne)) {
          word = word.substring(thirtyOneWordsLen);
          sequence.add("31");
        } else {
          isMatch = false;
          break;
        }
      }

      if (isMatch && validateSequence(sequence)) {
        matching++;
      }
    }

    return matching;
  }

  /**
   * Checks whether the sequence is valid and whether can be constructed from the rules (recursively or not).
   * Usually the sequence is like 42 42 .... 42 31 ..... 31 31, the number of 31s should be less then the 42s on the
   * right side.
   *
   * @param sequence the sequence to be checked
   * @return true if valid otherwise false
   */
  private static boolean validateSequence(List<String> sequence) {

    if (!sequence.contains("42")) {
      return false;
    }

    if (!sequence.contains("31")) {
      return false;
    }

    int i = sequence.indexOf("31");
    int countFortyTwo = 0;
    int countThirtyOne = 0;
    int j = i;
    int k = i - 1;
    for (; j < sequence.size(); j++) {
      if (sequence.get(j).equals("42")) {
        return false;
      }
      countThirtyOne++;
    }

    for (; k >= 0; k--) {
      if (sequence.get(k).equals("31")) {
        return false;
      }
      countFortyTwo++;
    }

    return countFortyTwo > countThirtyOne;
  }

  private static int getHowManyMatchesWithoutLoops(Map<String, Rule> rules, List<String> words) {

    int matching = 0;

    for (String word : words) {
      if (word.equals(tryCreatingWord("0", word, rules))) {
        matching++;
      }
    }

    return matching;
  }

  private static String tryCreatingWord(String ruleName, String word, Map<String, Rule> rules) {

    Rule rule = rules.get(ruleName);
    if (rule.isActualMatch) {
      return rule.matches.get(0).get(0);
    }

    boolean someMatch = false;
    String res = "";

    for (List<String> match : rule.matches) {
      boolean matchCurrent = true;
      StringBuilder m = new StringBuilder();
      String currentWord = word;

      for (String s : match) {
        String matcher = tryCreatingWord(s, currentWord, rules);
        int matcherLen = matcher.length();
        String temp = currentWord.substring(0, matcherLen);
        if (!temp.equals(matcher)) {
          matchCurrent = false;
          break;
        }
        currentWord = currentWord.substring(matcherLen);
        m.append(matcher);
      }

      if (matchCurrent) {
        someMatch = true;
        res = m.toString();
        break;
      }
    }

    if (!someMatch) {
      return "?";
    }

    return res;
  }

  private static List<String> getPossibleMatches(
    String ruleName, Map<String, Rule> rules) {

    Rule rule = rules.get(ruleName);

    if (rule.isActualMatch) {
      return Collections.singletonList(rule.matches.get(0).get(0));
    }

    List<String> res = new ArrayList<>();

    for (List<String> match : rule.matches) {
      List<List<String>> temp = new ArrayList<>();
      for (String s : match) {
        List<String> matchers = getPossibleMatches(s, rules);
        temp.add(matchers);
      }

      List<String> mergedMatchers = new ArrayList<>();
      mergeMatchers(temp, 0, mergedMatchers, "");

      res.addAll(mergedMatchers);
    }

    return res;
  }

  private static void mergeMatchers(
    List<List<String>> res, int i, List<String> mergedMatchers, String currentWord) {

    if (i == res.size()) {
      mergedMatchers.add(currentWord);
      return;
    }

    List<String> matching = res.get(i);

    for (String match : matching) {
      mergeMatchers(res, i + 1, mergedMatchers, currentWord + match);
    }

  }

  private static Map<String, Rule> parseRules(List<List<String>> groups) {

    List<String> strings = groups.get(0);
    Map<String, Rule> rules = new HashMap<>();

    for (String string : strings) {

      String[] split = string.split(":");
      String ruleIndex = split[0];
      String[] matchingString = split[1].split(" | ");
      List<List<String>> matching = new ArrayList<>();
      List<String> oneMatching = new ArrayList<>();
      boolean isActualMatch = false;

      for (String s : matchingString) {
        if (s.isEmpty()) {
          continue;
        }
        if (s.equals("|")) {
          matching.add(oneMatching);
          oneMatching = new ArrayList<>();
          continue;
        }

        String toAdd = s;

        if (s.contains("\"")) {
          toAdd = s.substring(1, s.length() - 1);
          isActualMatch = true;
        }

        oneMatching.add(toAdd);
      }

      matching.add(oneMatching);
      rules.put(ruleIndex, new Rule(isActualMatch, matching));
    }

    return rules;
  }

  private static class Rule {

    boolean isActualMatch;
    List<List<String>> matches;

    public Rule(boolean isActualMatch, List<List<String>> matches) {

      this.isActualMatch = isActualMatch;
      this.matches = matches;
    }
  }
}
