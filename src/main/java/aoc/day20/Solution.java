package aoc.day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import aoc.utils.Converter;
import aoc.utils.FileReader;

public class Solution {

  private static final List<Coordinate> SEA_MONSTER_PATTERN = Arrays.asList(
    Coordinate.newCoordinate(0, 18),
    Coordinate.newCoordinate(1, 0),
    Coordinate.newCoordinate(1, 5),
    Coordinate.newCoordinate(1, 6),
    Coordinate.newCoordinate(1, 11),
    Coordinate.newCoordinate(1, 12),
    Coordinate.newCoordinate(1, 17),
    Coordinate.newCoordinate(1, 18),
    Coordinate.newCoordinate(1, 19),
    Coordinate.newCoordinate(2, 1),
    Coordinate.newCoordinate(2, 4),
    Coordinate.newCoordinate(2, 7),
    Coordinate.newCoordinate(2, 10),
    Coordinate.newCoordinate(2, 13),
    Coordinate.newCoordinate(2, 16));

  private static long product = 1L;

  public static void main(String[] args) {

    List<List<String>> groups = FileReader.readLinesByGroups("day20");

    // part 1 is printed inside this method call
    int topLeft = findProductOfCornersAndFetchSomeCorner(groups);
    List<Tile> tiles = parseTiles(groups);
    int waterRoughness = getWaterRoughness(tiles, topLeft);
    System.out.println("Part 1: " + product);
    System.out.println("Part 2: " + waterRoughness);
  }

  private static int getWaterRoughness(List<Tile> tiles, int topLeft) {

    int count = 0;

    List<String> image = buildImage(tiles, topLeft);

    // find sea monsters
    char[][] imageWithSeaMonsters = findImageWithSeaMonsters(image);

    // count water roughness
    for (char[] imageWithSeaMonster : imageWithSeaMonsters) {
      for (int j = 0; j < imageWithSeaMonsters[0].length; j++) {
        if (imageWithSeaMonster[j] == '#') {
          count++;
        }
      }
    }

    return count;
  }

  private static char[][] findImageWithSeaMonsters(List<String> image) {

    for (int rot = 1; rot <= 4; rot++) {

      // original
      List<String> originalTile = image;

      List<Coordinate> seaMonstersCoordinates = checkSeaMonsterForTile(originalTile);

      if (!seaMonstersCoordinates.isEmpty()) {
        return markImageWithSeaMonsters(seaMonstersCoordinates, originalTile);
      }

      // flip vertical
      List<String> flippedVertical = new ArrayList<>();

      for (int i = originalTile.size() - 1; i >= 0; i--) {
        String s = originalTile.get(i);
        flippedVertical.add(s);
      }

      seaMonstersCoordinates = checkSeaMonsterForTile(flippedVertical);

      if (!seaMonstersCoordinates.isEmpty()) {
        return markImageWithSeaMonsters(seaMonstersCoordinates, flippedVertical);
      }

      // flip horizontal
      List<String> flippedHorizontal = new ArrayList<>();
      for (String s : originalTile) {
        String reversed = new StringBuilder(s).reverse().toString();
        flippedHorizontal.add(reversed);
      }

      seaMonstersCoordinates = checkSeaMonsterForTile(flippedHorizontal);

      if (!seaMonstersCoordinates.isEmpty()) {
        return markImageWithSeaMonsters(seaMonstersCoordinates, flippedHorizontal);
      }

      // rotate
      List<String> rotated = new ArrayList<>();

      for (int j = 0; j < originalTile.get(0).length(); j++) {
        StringBuilder currentRow = new StringBuilder();
        for (int i = originalTile.size() - 1; i >= 0; i--) {
          currentRow.append(originalTile.get(i).charAt(j));
        }
        rotated.add(currentRow.toString());
      }

      image = rotated;
    }

    throw new RuntimeException("Must find image with sea monsters");
  }

  private static char[][] markImageWithSeaMonsters(
    List<Coordinate> seaMonstersCoordinates,
    List<String> originalTile) {

    char[][] map = Converter.to2dMap(originalTile);

    for (Coordinate seaMonstersCoordinate : seaMonstersCoordinates) {
      map[seaMonstersCoordinate.x][seaMonstersCoordinate.y] = 'O';
    }

    return map;
  }

  private static List<Coordinate> checkSeaMonsterForTile(List<String> originalTile) {

    List<Coordinate> coordinates = new ArrayList<>();

    for (int i = 0; i < originalTile.size() - 2; i++) {

      for (int j = 0; j < originalTile.get(0).length() - 19; j++) {
        boolean isSeaMonster = true;

        for (Coordinate coordinate : SEA_MONSTER_PATTERN) {
          int dx = i + coordinate.x;
          int dy = j + coordinate.y;

          isSeaMonster &= originalTile.get(dx).charAt(dy) == '#';
        }

        if (isSeaMonster) {
          for (Coordinate coordinate : SEA_MONSTER_PATTERN) {
            int dx = i + coordinate.x;
            int dy = j + coordinate.y;
            coordinates.add(Coordinate.newCoordinate(dx, dy));
          }
        }
      }
    }

    return coordinates;
  }

  private static List<String> buildImage(List<Tile> tiles, int topLeft) {

    List<List<Tile>> imageMatrix = new ArrayList<>();

    imageMatrix.add(buildFirstRow(topLeft, tiles));

    // build other rows from the first
    populateRestOfTheImage(imageMatrix, tiles);

    // remove borders
    removeBordersFromTiles(imageMatrix);

    return toActualImage(imageMatrix);
  }

  private static List<String> toActualImage(List<List<Tile>> imageMatrix) {

    List<String> image = new ArrayList<>();

    for (List<Tile> matrix : imageMatrix) {

      for (int i = 0; i < matrix.get(0).tile.size(); i++) {
        StringBuilder row = new StringBuilder();

        for (Tile tile : matrix) {
          row.append(tile.tile.get(i));
        }

        image.add(row.toString());
      }
    }

    return image;
  }

  private static void removeBordersFromTiles(List<List<Tile>> imageMatrix) {

    for (List<Tile> matrix : imageMatrix) {
      for (Tile tile : matrix) {

        List<String> tileMatrix = new ArrayList<>();
        tile.tile.remove(0);
        tile.tile.remove(tile.tile.size() - 1);

        for (String s : tile.tile) {
          String substring = s.substring(1, s.length() - 1);
          tileMatrix.add(substring);
        }

        tile.tile = tileMatrix;
      }
    }

  }

  private static void populateRestOfTheImage(List<List<Tile>> imageMatrix, List<Tile> tiles) {

    SideSupplier topSideSupplier = tile -> tile.get(0);

    while (true) {
      boolean noRemainingTiles = tiles.stream().noneMatch(Tile::isNotVisited);
      if (noRemainingTiles) {
        break;
      }

      List<Tile> lastRow = imageMatrix.get(imageMatrix.size() - 1);
      List<Tile> newRow = new ArrayList<>();

      for (Tile aboveTile : lastRow) {
        String bottomSide = aboveTile.tile.get(aboveTile.tile.size() - 1);

        Tile bellowTile = findNextTile(tiles, bottomSide, topSideSupplier);

        if (bellowTile == null) {
          throw new RuntimeException("Must find matching tile");
        }

        bellowTile.visited = true;
        newRow.add(bellowTile);
      }

      imageMatrix.add(newRow);
    }

  }

  private static List<Tile> buildFirstRow(int topLeft, List<Tile> tiles) {

    Tile topLeftTile = tiles.get(topLeft);
    topLeftTile.visited = true;
    List<Tile> firstRowTiles = new ArrayList<>();
    firstRowTiles.add(topLeftTile);
    boolean isFirst = true;
    SideSupplier leftSideSupplier = tile -> getVerticalSide(tile, 0);
    SideSupplier bottomSideSupplier = tile -> tile.get(tile.size() - 1);

    while(true) {

      Tile rightTile = null;

      Tile tileToMatch = firstRowTiles.get(firstRowTiles.size() - 1);
      
      if (isFirst) {

        for (int rot = 1; rot <= 4; rot++) {

          // original
          List<String> originalTile = tileToMatch.tile;

          String rightSide = getVerticalSide(originalTile, originalTile.get(0).length() - 1);
          rightTile = findNextTile(tiles, rightSide, leftSideSupplier);
          String topSide = null;

          if (rightTile != null) {
            topSide = originalTile.get(0);
            Tile topTile = findNextTile(tiles, topSide, bottomSideSupplier);
            if (topTile == null) {
              tileToMatch.tile = originalTile;
              break;
            }
          }

          // flip vertical
          List<String> flippedVertical = new ArrayList<>();

          for (int i = originalTile.size() - 1; i >= 0; i--) {
            String s = originalTile.get(i);
            flippedVertical.add(s);
          }

          rightSide = getVerticalSide(flippedVertical, flippedVertical.get(0).length() - 1);
          rightTile = findNextTile(tiles, rightSide, leftSideSupplier);

          if (rightTile != null) {
            topSide = flippedVertical.get(0);
            Tile topTile = findNextTile(tiles, topSide, bottomSideSupplier);
            if (topTile == null) {
              tileToMatch.tile = flippedVertical;
              break;
            }
          }

          // flip horizontal
          List<String> flippedHorizontal = new ArrayList<>();
          for (String s : originalTile) {
            String reversed = new StringBuilder(s).reverse().toString();
            flippedHorizontal.add(reversed);
          }

          rightSide = getVerticalSide(flippedHorizontal, flippedHorizontal.get(0).length() - 1);
          rightTile = findNextTile(tiles, rightSide, leftSideSupplier);

          if (rightTile != null) {
            topSide = flippedHorizontal.get(0);
            Tile topTile = findNextTile(tiles, topSide, bottomSideSupplier);
            if (topTile == null) {
              tileToMatch.tile = flippedHorizontal;
              break;
            }
          }

          // rotate
          List<String> rotated = new ArrayList<>();

          for (int j = 0; j < originalTile.get(0).length(); j++) {
            StringBuilder currentRow = new StringBuilder();
            for (int i = originalTile.size() - 1; i >= 0; i--) {
              currentRow.append(originalTile.get(i).charAt(j));
            }
            rotated.add(currentRow.toString());
          }

          tileToMatch.tile = rotated;
        }

      } else {
        String rightSide = getVerticalSide(tileToMatch.tile, tileToMatch.tile.get(0).length() - 1);

        rightTile = findNextTile(tiles, rightSide, leftSideSupplier);
      }

      if (rightTile == null) {
        break;
      }

      rightTile.visited = true;
      firstRowTiles.add(rightTile);
      isFirst = false;
    }

    return firstRowTiles;
  }

  private static Tile findNextTile(List<Tile> tiles, String side, SideSupplier sideSupplier) {

    List<Tile> onlyNotVisited = tiles.stream().filter(Tile::isNotVisited).collect(Collectors.toList());

    for (Tile tile : onlyNotVisited) {

      // current rotation
      for (int rot = 1; rot <= 4; rot++) {

        // original
        List<String> originalTile = tile.tile;

        String otherSide = sideSupplier.getSide(originalTile);
        if (side.equals(otherSide)) {
          return tile;
        }

        // flip vertical
        List<String> flippedVertical = new ArrayList<>();

        for (int i = originalTile.size() - 1; i >= 0; i--) {
          String s = originalTile.get(i);
          flippedVertical.add(s);
        }

        otherSide = sideSupplier.getSide(flippedVertical);
        if (side.equals(otherSide)) {
          tile.tile = flippedVertical;
          return tile;
        }

        // flip horizontal
        List<String> flippedHorizontal = new ArrayList<>();
        for (String s : originalTile) {
          String reversed = new StringBuilder(s).reverse().toString();
          flippedHorizontal.add(reversed);
        }

        otherSide = sideSupplier.getSide(flippedHorizontal);
        if (side.equals(otherSide)) {
          tile.tile = flippedHorizontal;
          return tile;
        }

        // rotate
        List<String> rotated = new ArrayList<>();

        for (int j = 0; j < originalTile.get(0).length(); j++) {
          StringBuilder currentRow = new StringBuilder();
          for (int i = originalTile.size() - 1; i >= 0; i--) {
            currentRow.append(originalTile.get(i).charAt(j));
          }
          rotated.add(currentRow.toString());
        }

        tile.tile = rotated;
      }
    }

    return null;
  }

  private static String getVerticalSide(List<String> tile, int pos) {

    StringBuilder side = new StringBuilder();

    for (String s : tile) {
      side.append(s.charAt(pos));
    }

    return side.toString();
  }

  private static int findProductOfCornersAndFetchSomeCorner(List<List<String>> groups) {

    int indexTopLeft = -1;

    List<TileCorners> tiles = parseTilesCorners(groups);

    for (int i = 0; i < tiles.size(); i++) {
      TileCorners tile = tiles.get(i);
      int c = 0;
      boolean leftSideMatching = checkMatchingSide(i, tile.leftSide, tiles);
      if (leftSideMatching) {
        c++;
      }

      boolean rightSideMatching = checkMatchingSide(i, tile.rightSide, tiles);
      if (rightSideMatching) {
        c++;
      }

      boolean topSideMatching = checkMatchingSide(i, tile.top, tiles);
      if (topSideMatching) {
        c++;
      }

      boolean bottomSideMatching = checkMatchingSide(i, tile.bottom, tiles);
      if (bottomSideMatching) {
        c++;
      }

      if (c == 2) {

        indexTopLeft = i;
        product *= tile.id;
      }
    }

    return indexTopLeft;
  }

  private static List<TileCorners> parseTilesCorners(List<List<String>> groups) {

    List<TileCorners> tiles = new ArrayList<>();

    for (List<String> group : groups) {

      int tileId = Integer.parseInt(group.get(0).split(" ")[1].split(":")[0]);
      StringBuilder leftSide = new StringBuilder();
      StringBuilder rightSide = new StringBuilder();
      String top = group.get(1);
      String bot = group.get(group.size() - 1);

      for (int i = 1; i < group.size(); i++) {
        leftSide.append(group.get(i).charAt(0));
        rightSide.append(group.get(i).charAt(group.get(i).length() - 1));
      }

      tiles.add(new TileCorners(tileId, leftSide.toString(), rightSide.toString(), top, bot));
    }

    return tiles;
  }

  private static List<Tile> parseTiles(List<List<String>> groups) {

    List<Tile> tiles = new ArrayList<>();

    for (List<String> group : groups) {

      int tileId = Integer.parseInt(group.get(0).split(" ")[1].split(":")[0]);
      List<String> tile = new ArrayList<>();

      for (int i = 1; i < group.size(); i++) {
        tile.add(group.get(i));
      }

      tiles.add(new Tile(tileId, tile));
    }

    return tiles;
  }

  private static boolean checkMatchingSide(int index, String side, List<TileCorners> tiles) {

    for (int i = 0; i < tiles.size(); i++) {

      if (index == i) {
        continue;
      }

      boolean isMatching = side.equals(tiles.get(i).leftSide);
      isMatching |= side.equals(new StringBuilder(tiles.get(i).leftSide).reverse().toString());

      isMatching |= side.equals(tiles.get(i).rightSide);
      isMatching |= side.equals(new StringBuilder(tiles.get(i).rightSide).reverse().toString());

      isMatching |= side.equals(tiles.get(i).top);
      isMatching |= side.equals(new StringBuilder(tiles.get(i).top).reverse().toString());

      isMatching |= side.equals(tiles.get(i).bottom);
      isMatching |= side.equals(new StringBuilder(tiles.get(i).bottom).reverse().toString());

      if (isMatching) {
        return true;
      }
    }

    return false;
  }

  @FunctionalInterface
  private interface SideSupplier {

    String getSide(List<String> tile);
  }

  private static class Tile {

    private int id;
    private List<String> tile;
    private boolean visited;

    public Tile(int id, List<String> tile) {

      this.id = id;
      this.tile = tile;
    }

    public boolean isNotVisited() {

      return !visited;
    }
  }

  private static class TileCorners {

    private int id;
    private String leftSide;
    private String rightSide;
    private String top;
    private String bottom;

    public TileCorners(int id, String leftSide, String rightSide, String top, String bottom) {

      this.id = id;
      this.leftSide = leftSide;
      this.rightSide = rightSide;
      this.top = top;
      this.bottom = bottom;
    }
  }

  private static class Coordinate {

    int x;
    int y;

    public Coordinate(int x, int y) {

      this.x = x;
      this.y = y;
    }

    public static Coordinate newCoordinate(int x, int y) {
      return new Coordinate(x, y);
    }

  }

}
