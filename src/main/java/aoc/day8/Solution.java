package aoc.day8;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day8");
    Result result = getGlobalAccumulator(lines);
    int globalAccumulatorAfterFixingTheLoop = getGlobalAccumulatorAfterFixingTheLoop(lines);
    System.out.println("Part 1: " + result.accumulator);
    System.out.println("Part 2: " + globalAccumulatorAfterFixingTheLoop);
  }

  private static Result getGlobalAccumulator(List<String> lines) {

    Result result = new Result();

    Set<Integer> seen = new HashSet<>();

    for (int i = 0; i < lines.size(); ) {
      String[] instruction = lines.get(i).split(" ");
      String op = instruction[0];
      int argument = Integer.parseInt(instruction[1]);
      if (seen.contains(i)) {
        result.infiniteLoop = true;
        break;
      }

      if (op.equals("acc")) {
        result.accumulator += argument;
      }

      if (op.equals("jmp")) {
        i += argument;
        if (argument == 0) {
          result.infiniteLoop = true;
          break;
        }
        continue;
      }
      seen.add(i);
      i++;
    }

    return result;
  }

  private static int getGlobalAccumulatorAfterFixingTheLoop(List<String> lines) {

    for (int i = 0; i < lines.size(); i++) {
      String[] instruction = lines.get(i).split(" ");
      String op = instruction[0];
      String argument = instruction[1];
      List<String> newInstructions = new ArrayList<>(lines);
      boolean shouldRun = false;

      if (op.equals("jmp")) {
        shouldRun = true;
        newInstructions.set(i, "nop " + argument);
      }

      if (op.equals("nop")) {
        shouldRun = true;
        newInstructions.set(i, "jmp " + argument);
      }

      if (shouldRun) {
        Result result = getGlobalAccumulator(newInstructions);
        if (!result.infiniteLoop) {
          return result.accumulator;
        }
      }
    }

    return -1;
  }

  private static class Result {

    boolean infiniteLoop = false;
    int accumulator = 0;
  }
}
