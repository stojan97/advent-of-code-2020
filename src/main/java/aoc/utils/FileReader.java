package aoc.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

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

}
