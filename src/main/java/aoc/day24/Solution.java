package aoc.day24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  private static final Map<String, Integer> DX = Map.of(
    "w", -1,
    "e", 1,
    "sw", -1,
    "se", 0,
    "nw", 0,
    "ne", 1);


  private static final Map<String, Integer> DY = Map.of(
    "w", 1,
    "e", -1,
    "sw", 0,
    "se", -1,
    "nw", 1,
    "ne", 0);

  private static final Map<String, Integer> DZ = Map.of(
    "w", 0,
    "e", 0,
    "sw", 1,
    "se", 1,
    "nw", -1,
    "ne", -1);

  private static final List<Integer> DX_VALUES = new ArrayList<>(DX.values());

  private static final List<Integer> DY_VALUES = new ArrayList<>(DY.values());

  private static final List<Integer> DZ_VALUES = new ArrayList<>(DZ.values());

  public static void main(String[] args) {

    List<String> input = FileReader.readLines("day24");

    Map<Tile, Boolean> tiles = getTiles(input);
    long blackTiles = tiles.values().stream().filter(Boolean::booleanValue).count();
    Map<Tile, Integer> tilesWithCounter = new HashMap<>();

    for (Map.Entry<Tile, Boolean> entry : tiles.entrySet()) {
      Tile current = entry.getKey();
      current.isBlackTile = entry.getValue();
      tilesWithCounter.put(current, 0);
    }

    int blackTilesAfterSimulation = getBlackTilesAfterHundredDays(tilesWithCounter, 100);

    System.out.println("Part 1: " + blackTiles);
    System.out.println("Part 2: " + blackTilesAfterSimulation);
  }

  private static int getBlackTilesAfterHundredDays(Map<Tile, Integer> tiles, int times) {

    Map<Tile, Integer> changingTiles = new HashMap<>(tiles);

    for (Map.Entry<Tile, Integer> entry : tiles.entrySet()){

      boolean isBlack = entry.getKey().isBlackTile;

      if (!isBlack) {
        continue;
      }

      Tile currentTile = entry.getKey();

      for (int i = 0; i < DX_VALUES.size(); i++) {

        int dx = currentTile.x + DX_VALUES.get(i);
        int dy = currentTile.y + DY_VALUES.get(i);
        int dz = currentTile.z + DZ_VALUES.get(i);

        Tile otherTile = Tile.createTile(dx, dy, dz);

        int blackTiles = changingTiles.getOrDefault(otherTile, 0);
        changingTiles.put(otherTile, blackTiles + 1);
      }

    }

    int blackTiles = 0;

    for (Map.Entry<Tile, Integer> entry : changingTiles.entrySet()) {

      Tile currentTile = entry.getKey();
      boolean isBlack = currentTile.isBlackTile;
      int blackTilesForCurrent = entry.getValue();

      if (isBlack && (blackTilesForCurrent == 0 || blackTilesForCurrent > 2)) {
        currentTile.isBlackTile = false;
      }

      if (!isBlack && blackTilesForCurrent == 2) {
        currentTile.isBlackTile = true;
      }

      if (currentTile.isBlackTile) {
        blackTiles++;
      }

      entry.setValue(0);
    }

    if (times > 1) {
      return getBlackTilesAfterHundredDays(changingTiles, times - 1);
    }

    return blackTiles;
  }

  private static Map<Tile, Boolean> getTiles(List<String> instructions) {

    Map<Tile, Boolean> tiles = new HashMap<>();

    for (String tile : instructions) {

      int x = 0;
      int y = 0;
      int z = 0;

      String ins = "";

      for (char c : tile.toCharArray()) {
        ins += c;
        if (DX.containsKey(ins)) {
          x += DX.get(ins);
          y += DY.get(ins);
          z += DZ.get(ins);
          ins = "";
        }
      }

      Tile currentTile = Tile.createTile(x, y, z);
      boolean isFlipped = tiles.getOrDefault(currentTile, false);

      tiles.put(currentTile, !isFlipped);
    }

    return tiles;
  }


  private static class Tile {
    private int x;
    private int y;
    private int z;
    private boolean isBlackTile;

    private Tile(int x, int y, int z) {

      this.x = x;
      this.y = y;
      this.z = z;
    }

    public static Tile createTile(int x, int y, int z) {
      return new Tile(x, y, z);
    }

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (!(o instanceof Tile)) {
        return false;
      }
      Tile tile = (Tile) o;
      return x == tile.x && y == tile.y && z == tile.z;
    }

    @Override
    public int hashCode() {

      return Objects.hash(x, y, z);
    }
  }


}
