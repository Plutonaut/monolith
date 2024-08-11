package com.game.utils.application;

import java.io.File;

public class PathSanitizer {
  public static String sanitizeFilePath(String path) {
    return sanitizeFilePath(path, "");
  }

  public static String sanitizeFilePath(String path, String subDirectory) {
    if (LoaderUtils.isResourcePath(path)) return path;

    StringBuilder builder = new StringBuilder(path);

    prependDirectory(subDirectory, builder);

    for (String directory : LoaderUtils.RESOURCE_DIR_ARR) prependDirectory(directory, builder);

    return builder.toString();
  }

  static void prependDirectory(String directory, StringBuilder builder) {
    if (!builder.toString().contains(directory)) {
      if (!builder.toString().startsWith(File.separator)) builder.insert(0, File.separator);
      builder.insert(0, directory);
    }
  }
}
