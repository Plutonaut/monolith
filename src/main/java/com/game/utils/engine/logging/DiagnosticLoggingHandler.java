package com.game.utils.engine.logging;

import com.game.utils.logging.PrettifyUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DiagnosticLoggingHandler extends AbstractLoggingHandler {
  @Override
  public void init(String fileName) {
    create(fileName, "/diagnostics");
  }

  @Override
  protected void print() {
    writes++;

    row("Log", writes);

    stream.print(builder.append(System.lineSeparator()));
    builder = null;
  }

  public DiagnosticLoggingHandler open(String caller) {
    close();

    builder = title(caller);
    return this;
  }

  public DiagnosticLoggingHandler message(String message) {
    return row("Message", message);
  }

  public DiagnosticLoggingHandler row(String label, Quaternionf value) {
    return row(label, PrettifyUtils.prettify(value));
  }

  public DiagnosticLoggingHandler row(String label, Vector4f value) {
    return row(label, PrettifyUtils.prettify(value));
  }

  public DiagnosticLoggingHandler row(String label, Vector3f value) {
    return row(label, PrettifyUtils.prettify(value));
  }

  public DiagnosticLoggingHandler row(String label, float value) {
    return row(label, PrettifyUtils.prettify(value));
  }

  public DiagnosticLoggingHandler row(String label, int value) {
    return row(label, Integer.toString(value));
  }

  public DiagnosticLoggingHandler row(String label, boolean value) {
    if (builder != null) {
      builder.append("\n\t").append(label).append(": ").append(value);
      return this;
    }
    return null;
  }

  public DiagnosticLoggingHandler row(String label) {
    if (builder != null) {
      builder.append("\n\t").append(label);
      return this;
    }

    return null;
  }

  public DiagnosticLoggingHandler row(String label, String element) {
    if (builder != null) {
      builder.append("\n\t").append(label).append(": ").append(element);
      return this;
    }

    return null;
  }
}
