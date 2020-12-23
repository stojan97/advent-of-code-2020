package aoc.utils;

import java.util.List;

public final class Converter {

  private Converter() {

  }

  public static char[][] to2dMap(List<String> lines) {

    char[][] map = new char[lines.size()][lines.get(0).length()];

    for (int i = 0; i < lines.size(); i++) {
      for (int j = 0; j < lines.get(i).length(); j++) {
        map[i][j] = lines.get(i).charAt(j);
      }
    }

    return map;
  }

}
