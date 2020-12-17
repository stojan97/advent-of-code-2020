package aoc.day17;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  private static final int[] DX = { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
  private static final int[] DY = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };
  private static final int MAX_CYCLE = 6;

  public static void main(String[] args) {

    char[][] grid = FileReader.readLinesAs2dMap("day17");

    Map<Cube, Integer> initialCubes = parseInitialCubes(grid);
    Map<Cube, Integer> initialCubesCopy = new HashMap<>();
    for (Map.Entry<Cube, Integer> entry : initialCubes.entrySet()) {
      Cube cube = entry.getKey();
      initialCubesCopy.put(new Cube(cube.x, cube.y, cube.z, cube.w, cube.isActive), 0);
    }

    int activeCubes = getActiveCubes(initialCubes, 1, false);
    int activeCubesInFourthDimension = getActiveCubes(initialCubesCopy, 1, true);

    System.out.println("Part 1: " + activeCubes);
    System.out.println("Part 2: " + activeCubesInFourthDimension);
  }

  private static int getActiveCubes(Map<Cube, Integer> initialCubes, int cycle, boolean isFourthDimension) {

    Map<Cube, Integer> changingCubesMap = new HashMap<>(initialCubes);

    for (Map.Entry<Cube, Integer> cubeEntry : initialCubes.entrySet()) {

      Cube cube = cubeEntry.getKey();

      if (!cube.isActive) {
        continue;
      }

      int maxW = (isFourthDimension) ? 1 : 0;
      int minW = (isFourthDimension) ? -1 : 0;

      for (int w = minW; w <= maxW; w++) {
        for (int z = -1; z <= 1; z++) {
          for (int k = 0; k < DX.length; k++) {
            int dx = cube.x + DX[k];
            int dy = cube.y + DY[k];
            int dz = cube.z + z;
            int dw = cube.w + w;
            Cube otherCube = Cube.constructCube(dx, dy, dz, dw);

            if (cube.equals(otherCube)) {
              continue;
            }

            int value = changingCubesMap.getOrDefault(otherCube, 0);
            changingCubesMap.put(otherCube, value + 1);
          }
        }
      }

    }

    int activeCubesCount = 0;

    for (Map.Entry<Cube, Integer> cubeEntry : changingCubesMap.entrySet()) {

      Cube cube = cubeEntry.getKey();
      int activeCubes = cubeEntry.getValue();

      if (cube.isActive) {
        cube.isActive = activeCubes == 2 || activeCubes == 3;
      } else {
        cube.isActive = activeCubes == 3;
      }

      if (cube.isActive) {
        activeCubesCount++;
      }

      cubeEntry.setValue(0);
    }

    if (cycle < MAX_CYCLE) {
      return getActiveCubes(changingCubesMap, cycle + 1, isFourthDimension);
    }

    return activeCubesCount;
  }

  private static Map<Cube, Integer> parseInitialCubes(char[][] grid) {

    List<Cube> cubes = new ArrayList<>();

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        cubes.add(new Cube(i, j, 0, 0, grid[i][j] == '#'));
      }
    }

    return cubes.stream().collect(Collectors.toMap(cube -> cube, integer -> 0));
  }

  private static class Cube {

    int x;
    int y;
    int z;
    int w;
    boolean isActive;

    public Cube(int x, int y, int z, int w, boolean isActive) {

      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
      this.isActive = isActive;
    }

    private Cube(int x, int y, int z, int w) {

      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
    }

    public static Cube constructCube(int x, int y, int z, int w) {

      return new Cube(x, y, z, w);
    }

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (!(o instanceof Cube)) {
        return false;
      }
      Cube cube = (Cube) o;
      return x == cube.x && y == cube.y && z == cube.z && w == cube.w;
    }

    @Override
    public int hashCode() {

      return Objects.hash(x, y, z, w);
    }
  }
}
