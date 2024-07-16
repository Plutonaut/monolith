package com.game.utils.application;

import com.game.loaders.ILineIngestor;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoaderUtils {
  static final String[] RESOURCE_DIR_ARR = new String[]{
    "resources", "main", "src"
  };

  public static boolean isResourcePath(String path) {
    return path != null && !path.isEmpty() && Files.exists(Path.of(path));
  }

  public static boolean isUrl(String path) {
    return new UrlValidator().isValid(path);
  }

  static String parsePathParentName(String path) {
    return !path.contains(File.separator) ? path : path.substring(0, path.lastIndexOf(File.separator));
  }

  public static String getParentDirectory(String path) {
    return !Files.exists(Path.of(path)) ? parsePathParentName(path) : new File(path).getParent();
  }

  public static String getFileType(String path) {
    return !path.contains(".") ? null : path.substring(path.indexOf("."));
  }

  public static ILineIngestor readFromFile(String path, ILineIngestor ingestor) {
    try (InputStream in = new FileInputStream(path);
         BufferedReader reader = new BufferedReader(new InputStreamReader((in)))) {
      String line;
      while ((line = reader.readLine()) != null) ingestor.read(sanitizeLine(line));
    } catch (IOException e) {
      throw new RuntimeException(("Failed to read from file at path: " + path));
    }
    return ingestor;
  }

  static String sanitizeLine(String line) {
    if (line == null)
      throw new RuntimeException("Error occurred while reading file input. Unable to sanitize line as it was null!");

    String sanitized = line;
    if (!line.endsWith("\n")) sanitized = sanitized + "\n";
    return sanitized;
  }

  public static String load(String path) {
    return load(path, "");
  }

  public static String load(String path, String subDirectory) {
    if (isUrl(path))
      throw new UnsupportedOperationException("Could not load data from " + path + " as urls are not yet supported!");
    return loadResourceAsStr(PathSanitizer.sanitizeFilePath(path, subDirectory));
  }

  static String loadResourceAsStr(String path) {
    final StringBuilder builder = new StringBuilder();

    readFromFile(path, builder::append);
    return builder.toString();
  }
}
