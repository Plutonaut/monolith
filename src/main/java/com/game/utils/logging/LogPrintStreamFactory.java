package com.game.utils.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class LogPrintStreamFactory {
  public static PrintStream create(String logFileName, String directory, String fileType, int maxLogFiles, boolean appendDate) {
    try {
      prune(directory, maxLogFiles);
      StringBuilder builder = new StringBuilder(directory).append(File.separator).append(logFileName);
      if (appendDate) builder.append("_").append(LocalDateTime.now().format(LoggingUtils.LOG_DATE_TIME_FORMATTER));
      String fileName = builder.append(fileType).toString();
      return new PrintStream(new FileOutputStream(fileName));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void prune(String path, int maxLogFiles) throws IOException {
    List<Path> files = LoggingUtils.getFileSet(path);
    int size = files.size();

    if (size > maxLogFiles)
      files.subList(maxLogFiles, size).forEach(LogPrintStreamFactory::deleteIfMaxFileCountReached);
    else files.forEach(LogPrintStreamFactory::deleteLogFileOlderThan);
  }

  static void deleteIfMaxFileCountReached(Path path) {
    LoggingUtils.attemptLogDeletion(path, "Max file count has been reached. Deleting file [{}]");
  }

  static void deleteLogFileOlderThan(Path path) {
    LocalDateTime logDateTime = LoggingUtils.getDateOfLog(path);

    if (logDateTime.isBefore(LoggingUtils.DELETE_IF_OLDER_THAN))
      LoggingUtils.attemptLogDeletion(path, "Log file [{}] was made before [{}]. Deleting file.");
  }
}
