package aoc.day18;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> expressions = FileReader.readLines("day18");

    List<List<String>> expressionsList = parseInput(expressions);
    long sum = getSumWithoutPrecedence(expressionsList);
    long sumWithPrecedence = getSumWithPrecedence(expressionsList);

    System.out.println("Part 1: " + sum);
    System.out.println("Part 2: " + sumWithPrecedence);
  }

  private static long getSumWithoutPrecedence(List<List<String>> expressionsList) {

    long sum = 0;
    for (List<String> expression : expressionsList) {

      Stack<String> symbols = new Stack<>();
      Stack<Long> numbers = new Stack<>();

      for (String current : expression) {
        boolean isNumber = current.chars().allMatch(Character::isDigit);

        if (isNumber) {
          long currentNumber = Long.parseLong(current);
          if (symbols.empty() || symbols.peek().equals("(")) {
            numbers.push(currentNumber);
          } else {
            putNumber(symbols, numbers, currentNumber);
          }

        } else if (current.equals(")")) {
          symbols.pop();
          if (isTopSymbolOperation(symbols)) {
            putNumber(symbols, numbers, numbers.pop());
          }
        } else {
          symbols.push(current);
        }
      }

      sum += numbers.pop();
    }

    return sum;
  }

  private static long getSumWithPrecedence(List<List<String>> expressionsList) {

    long sum = 0;
    for (List<String> expression : expressionsList) {

      Stack<String> symbols = new Stack<>();
      Stack<Long> numbers = new Stack<>();

      for (int i = 0; i < expression.size(); i++) {
        String current = expression.get(i);
        boolean isNumber = current.chars().allMatch(Character::isDigit);

        if (isNumber) {
          long currentNumber = Long.parseLong(current);
          if (symbols.empty() || symbols.peek().equals("(")) {
            numbers.push(currentNumber);
          } else {
            if (i + 1 < expression.size() && symbols.peek().equals("*") && expression.get(i + 1).equals("+")) {
              numbers.push(currentNumber);
              continue;
            }
            putNumber(symbols, numbers, currentNumber);
          }

        } else if (current.equals(")")) {
          while (!symbols.peek().equals("(")) {
            putNumber(symbols, numbers, numbers.pop());
          }
          symbols.pop();

          if (isTopSymbolOperation(symbols)) {
            if (i + 1 < expression.size() && symbols.peek().equals("*") && expression.get(i + 1).equals("+")) {
              continue;
            }
            putNumber(symbols, numbers, numbers.pop());
          }
        } else {
          if (current.equals("(") && isTopSymbolOperation(symbols)) {
            String top = symbols.pop();
            long number = -1;
            String mul = "";

            if (top.equals("+") && !symbols.isEmpty() && symbols.peek().equals("*")) {
              mul = symbols.pop();
              number = numbers.pop();
            }

            while (!symbols.isEmpty() && !symbols.peek().equals("(") && numbers.size() > 1) {
              putNumber(symbols, numbers, numbers.pop());
            }
            if (number != -1) {
              numbers.push(number);
              symbols.push(mul);
            }
            symbols.push(top);
          }
          symbols.push(current);
        }
      }

      while(!symbols.isEmpty()) {
        putNumber(symbols, numbers, numbers.pop());
      }

      sum += numbers.pop();
    }

    return sum;
  }

  private static boolean isTopSymbolOperation(Stack<String> symbols) {

    return !symbols.isEmpty() && (symbols.peek().equals("+") || symbols.peek().equals("*"));
  }

  private static void putNumber(Stack<String> symbols, Stack<Long> numbers, long currentNumber) {

    Long top = numbers.pop();
    String topSymbol = symbols.pop();

    if (topSymbol.equals("+")) {
      currentNumber += top;
    } else if (topSymbol.equals("*")) {
      currentNumber *= top;
    }

    numbers.push(currentNumber);
  }

  private static List<List<String>> parseInput(List<String> expressions) {

    List<List<String>> expressionsList = new ArrayList<>();

    for (String expression : expressions) {
      String[] s = expression.split(" ");
      List<String> listOfAll = new ArrayList<>();

      for (String s1 : s) {
        String[] firstSplit = s1.split("\\(");

        for (char c : s1.toCharArray()) {
          if (c == '(') {
            listOfAll.add(c + "");
          }
        }

        for (String fs : firstSplit) {
          String[] split = fs.split("\\)");

          for (String s2 : split) {
            if (!s2.isEmpty()) {
              listOfAll.add(s2);
            }
          }
        }

        for (char c : s1.toCharArray()) {
          if (c == ')') {
            listOfAll.add(c + "");
          }
        }
      }
      expressionsList.add(listOfAll);
    }

    return expressionsList;
  }
}
