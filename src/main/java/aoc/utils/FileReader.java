package aoc.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FileReader {

  private FileReader() {

  }

  public static List<String> readLines(String day) {

    String path = String.format("aoc/%s/input.txt", day);

    ClassLoader classLoader = FileReader.class.getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());

    try {
      return Files.readAllLines(file.toPath());
    } catch (IOException e) {
      String msg = String.format("There was issue when reading the input file: %s", path);
      throw new RuntimeException(msg);
    }
  }

  public static List<List<String>> readLinesByGroups(String path) {

    List<String> lines = readLines(path);
    List<List<String>> result = new ArrayList<>();
    List<String> toAddList = new ArrayList<>();

    for (String line : lines) {
      if (line.isEmpty()) {
        result.add(toAddList);
        toAddList = new ArrayList<>();
      } else {
        toAddList.add(line);
      }

    }

    result.add(toAddList);

    return result;
  }

  public static char[][] readLinesAs2dMap(String path) {

    List<String> lines = readLines(path);

    char[][] map = new char[lines.size()][lines.get(0).length()];

    for (int i = 0; i < lines.size(); i++) {
      for (int j = 0; j < lines.get(i).length(); j++) {
        map[i][j] = lines.get(i).charAt(j);
      }
    }

    return map;
  }

  public static List<Character> toListOfChars(String line) {

    return line.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
  }

}
