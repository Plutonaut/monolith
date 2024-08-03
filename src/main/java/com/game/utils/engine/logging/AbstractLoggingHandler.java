package com.game.utils.engine.logging;

import com.game.utils.application.IPersistable;
import com.game.utils.application.PathSanitizer;
import com.game.utils.logging.LogPrintStreamFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;

@Slf4j
public abstract class AbstractLoggingHandler {
  static final String SEPARATOR = "\n======================================================================================\n";

  protected StringBuilder builder;
  protected PrintStream stream;

  protected int writes;

  private static final String ON_CLOSE_MSG = "Closing Print Stream";

  public abstract void init(String logFileName);

  protected StringBuilder title(String id) {
    return new StringBuilder(SEPARATOR).append("#\t")
                                                    .append(id)
                                                    .append(SEPARATOR);
  }

  protected void create(String logFileName, String directory) {
    create(logFileName, directory, ".log");
  }

  protected void create(String logFileName, String directory, String fileType) {
    create(logFileName, directory, fileType, 2, false);
  }

  protected void create(String logFileName, String directory, String fileType, int maxLogFiles, boolean appendDate) {
    dispose();
    String sanitizedDirectory = PathSanitizer.sanitizeFilePath(directory, "/logs");
    stream = LogPrintStreamFactory.create(logFileName, sanitizedDirectory, fileType, maxLogFiles, appendDate);
  }

  public void save(IPersistable data) {
    dispose();

    init(data.id());

    builder = title(data.id());
    data.write(builder);

    print();
    dispose();
  }

  protected abstract void print();

  public void console(String message) {
    log.info(message);
  }

  public void close() {
    if (builder != null)
      print();
  }

  public void dispose(String message) {
    console(message);

    dispose();
  }

  public void dispose() {
    if (stream != null) {
      stream.print(ON_CLOSE_MSG);
      log.debug(ON_CLOSE_MSG);

      stream.close();
      stream = null;
    }
  }
}
