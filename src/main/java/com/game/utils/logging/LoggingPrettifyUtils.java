package com.game.utils.logging;

import java.text.DecimalFormat;

public class LoggingPrettifyUtils {
  static final DecimalFormat df = new DecimalFormat("#.##");
  public static String prettify(float f) {
    return df.format(f);
  }
}
