package com.game.utils.logging;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class LoggingUtils {
  public static final LocalDateTime DELETE_IF_OLDER_THAN = LocalDateTime.now().minusWeeks(1);
  public static final String SIMPLE_DATE = "MM-dd-yyyy";
  public static final String SIMPLE_TIME = "-HH-mm";
  public static final String TIME_COMPLETE = "-ss";
  public static final Pattern FILE_DATE_RGX = Pattern.compile(
    ".*((0[1-9]|1[012])-\\d{2}-20[0-9]{2}(-[0-9]{2})+).*\\.log$");
  public static final DateTimeFormatter LOG_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(SIMPLE_DATE + SIMPLE_TIME + TIME_COMPLETE);
  public static final String BASE_LOG_DIRECTORY = "src/main/resources/logs";
  public static final String WINDOW_LOG_DIRECTORY = BASE_LOG_DIRECTORY + "/opengl";
  public static final String WINDOW_LOG_FILENAME = "game_engine_window";

  public static boolean isLoggingFile(Path path) {
    return !Files.isDirectory(path) && FILE_DATE_RGX.matcher(path.toString()).matches();
  }

  public static int compareLogDates(Path pathA, Path pathB) {
    return getDateOfLog(pathA).compareTo(getDateOfLog(pathB));
  }

  public static List<Path> getFileSet(String dir) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
      return stream.filter(LoggingUtils::isLoggingFile).sorted(LoggingUtils::compareLogDates).toList();
    }
  }

  public static LocalDateTime getDateOfLog(Path path) {
    Matcher matcher = FILE_DATE_RGX.matcher(path.toString());
    if (!matcher.matches()) {
      log.warn("Could not find a match for log file date [{}}", path);
      return LocalDateTime.now();
    }
    String g = matcher.group(1);
    StringBuilder strFormatBuilder = new StringBuilder(SIMPLE_DATE);

    if (g.length() > 10) strFormatBuilder.append(SIMPLE_TIME);
    if (g.length() > 16) strFormatBuilder.append(TIME_COMPLETE);

    return LocalDateTime.parse(g, DateTimeFormatter.ofPattern(strFormatBuilder.toString()));
  }

  public static void attemptLogDeletion(Path path, String message) {
    Path absolutePath = path.toAbsolutePath();
    if (Files.exists(absolutePath)) {
      log.info(message, path);
      try {
        Files.delete(absolutePath);
      } catch (IOException exception) {
        log.error("Could not delete file at path {}\n{}", absolutePath, exception.getMessage());
        throw new RuntimeException(exception);
      }
    }
  }
}
