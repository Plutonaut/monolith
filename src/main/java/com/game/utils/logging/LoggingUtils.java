package com.game.utils.logging;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
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
  public static final String SIMPLE_DATE = "MM-dd-yyyy";
  public static final String SIMPLE_TIME = "-HH-mm";
  public static final String TIME_COMPLETE = "-ss";
  public static final Pattern FILE_DATE_RGX = Pattern.compile(
    ".*((0[1-9]|1[012])-\\d{2}-20[0-9]{2}(-[0-9]{2})+).*\\.log$");
  public static final DateTimeFormatter READABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
  public static final DateTimeFormatter LOG_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
    SIMPLE_DATE + SIMPLE_TIME + TIME_COMPLETE);
  public static final LocalDateTime DELETE_IF_OLDER_THAN = LocalDateTime.now().minusWeeks(1);
  public static final String OLDER_THAN_DATE_READABLE_FORMAT = DELETE_IF_OLDER_THAN.format(READABLE_DATE_FORMATTER);
  public static final String BASE_LOG_DIRECTORY = "src/main/resources/logs";
  public static final String WINDOW_LOG_DIRECTORY = BASE_LOG_DIRECTORY + "/opengl";
  public static final String WINDOW_LOG_FILENAME = "game_engine_window";
  static final String LOG_DELETION_ERROR_MESSAGE = "Could not delete file at path %s because file %s";

  public static boolean isLoggingFile(Path path) {
    return !Files.isDirectory(path) && FILE_DATE_RGX.matcher(path.toString()).matches();
  }

  public static int compareLogDates(Path pathA, Path pathB) {
    return getDateOfLog(pathA).compareTo(getDateOfLog(pathB));
  }

  public static List<Path> getFileSet(String dir) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
      return stream
        .filter(LoggingUtils::isLoggingFile)
        .sorted(LoggingUtils::compareLogDates)
        .toList();
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

  public static boolean logFileIsEmpty(Path path) {
    Path absolutePath = path.toAbsolutePath();
    File f = absolutePath.toFile();
    return f.exists() && f.isFile() && f.length() < 1;
  }

  public static void attemptLogDeletion(Path path, String message) {
    Path absolutePath = path.toAbsolutePath();
    File f = absolutePath.toFile();
    String errorMessage = onLogFileDeletion(f);

    if (!errorMessage.isEmpty()) {
      throw new IllegalArgumentException(LOG_DELETION_ERROR_MESSAGE.formatted(absolutePath,
                                                                               errorMessage
      ));
    }

    log.info(message, path);
  }

  static String onLogFileDeletion(File f) {
    String errorMessage = "";

    if (!f.exists()) errorMessage = "does note exist!";
    else if (!f.canWrite()) errorMessage = "is write protected!";
    else if (f.isDirectory()) errorMessage = "is a directory!";
    else if (!f.delete()) errorMessage = "delete operation resulted in an I/O exception!";

    return errorMessage;
  }
}
