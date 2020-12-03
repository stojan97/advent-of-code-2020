package aoc.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FileReader {

  private FileReader() {

  }

  public static List<String> readLines(String path) {

    ClassLoader classLoader = FileReader.class.getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());

    try {
      return Files.readAllLines(file.toPath());
    } catch (IOException e) {
      String msg = String.format("There was issue when reading the input file: %s", path);
      throw new RuntimeException(msg);
    }
  }

  public static List<List<Character>> readLinesAs2dMap(String path) {

    List<String> lines = readLines(path);

    return lines
      .stream()
      .map(FileReader::toListOfChars)
      .collect(Collectors.toList());
  }

  public static List<Character> toListOfChars(String line) {

    return line
      .chars()
      .mapToObj(c -> (char) c)
      .collect(Collectors.toList());
  }

}
