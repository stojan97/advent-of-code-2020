package aoc.day16;

import java.util.Set;

public class Field {

  private final String fieldName;
  private final Set<Integer> positions;

  public Field(String fieldName, Set<Integer> positions) {

    this.fieldName = fieldName;
    this.positions = positions;
  }

  public Set<Integer> getPositions() {

    return positions;
  }

  public void removeAllInPositions(Set<Integer> seenPositions) {
    positions.removeAll(seenPositions);
  }

  public int getPositionsSize() {
    return positions.size();
  }

  public boolean fieldNameContains(String substring) {
    return fieldName.contains(substring);
  }

}
