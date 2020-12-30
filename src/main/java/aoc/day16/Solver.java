package aoc.day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a bonus class for solving part 2 which was intended to be solved with greedy approach. It was obvious that
 * the fields in part 2 can be represented as a two sets of nodes where want to match all the nodes on the left (fields)
 * with the ones on the right (positions).
 */
public final class Solver {

  public static long solve(List<Field> fields, List<Integer> myTicketValues, Approach approach) {

    switch (approach) {
      case GREEDY:
        return solveGreedy(fields, myTicketValues);
      case BIPARTITE_MATCHING:
        return solveBipartiteMatching(fields, myTicketValues);
      default:
        throw new IllegalStateException("Some issue");
    }
  }

  private static long solveBipartiteMatching(List<Field> fields, List<Integer> myTicketValues) {

    int n = fields.size();
    int m = fields.stream().map(Field::getPositions).flatMap(Set::stream).collect(Collectors.toSet()).size();

    int[] matchingFields = new int[n];
    int[] matchingPositions = new int[m];
    Arrays.fill(matchingFields, -1);
    Arrays.fill(matchingPositions, -1);

    // matching fields
    for (int i = 0; i < n; i++) {
      if (matchingFields[i] != -1) {
        continue;
      }
      Set<Integer> visitedFields = new HashSet<>();
      bipartiteMatching(i, fields, visitedFields, matchingFields, matchingPositions);
    }

    // get result
    long res = 1L;

    for (int i = 0; i < n; i++) {
      int position = matchingFields[i];

      if (fields.get(i).fieldNameContains("departure")) {
        res *= myTicketValues.get(position);
      }
    }

    return res;
  }

  private static boolean bipartiteMatching(
    int current,
    List<Field> fields,
    Set<Integer> visitedFields,
    int[] matchingFields,
    int[] matchingPositions) {

    if (visitedFields.contains(current)) {
      return false;
    }

    visitedFields.add(current);

    for (int pos : fields.get(current).getPositions()) {
      if (matchingPositions[pos] == -1
          || bipartiteMatching(matchingPositions[pos], fields, visitedFields, matchingFields, matchingPositions)) {

        matchingPositions[pos] = current;
        matchingFields[current] = pos;
        return true;
      }
    }

    return false;
  }

  private static long solveGreedy(List<Field> fields, List<Integer> myTicketValues) {

    fields.sort((field1, field2) -> Integer.compare(field1.getPositionsSize(), field2.getPositionsSize()));
    long res = 1L;
    Set<Integer> seen = new HashSet<>();

    for (Field field : fields) {

      field.removeAllInPositions(seen);

      if (field.getPositionsSize() != 1) {
        String msg = "Field positions should contain only one element";
        throw new RuntimeException(msg);
      }
      List<Integer> resList = new ArrayList<>(field.getPositions());
      int currentValue = resList.get(0);

      if (field.fieldNameContains("departure")) {
        res *= myTicketValues.get(currentValue);
      }

      seen.add(currentValue);
    }

    return res;
  }

  enum Approach {

    /**
     * Greedy approach is the one where you analyze the fields and observe that you can you solve with greedy style by
     * process of eliminating the ones that can be matched. This was the intended solution by the author and my first
     * solution during the AOC run.
     */
    GREEDY,

    /**
     * This is a bonus approach which is called maximum bipartite matching, and is used when the two sets do not have
     * the property where we can deduce the matching's greedily so we assume that the sets do not have perfect matching
     * (meaning to have all nodes matched in both sets) and proceed to find the maximum possible matching between the
     * two sets.
     */
    BIPARTITE_MATCHING
  }
}
