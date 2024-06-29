package com.game.engine.window;

import org.apache.commons.lang3.SystemUtils;

public record EngineGLVersion(int x, int y) {
  public boolean forwardCompatibility() {
    return x <= 3 || y <= 5;
  }

  public boolean openCompatibility() {
    return x <= 2 && y <= 1 && SystemUtils.IS_OS_MAC_OSX;
  }

  public double toDouble() {
    return x + y / 10.0;
  }

  @Override
  public String toString() {
    return String.format("GL Version {%d}.{%d}", x, y);
  }

  public static EngineGLVersion toGLVersion(String str) {
    final String[] s = str.split("\\.");

    return new EngineGLVersion(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
  }
}
