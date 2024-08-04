package com.game.utils.logging;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.text.DecimalFormat;

public class LoggingPrettifyUtils {
  static final DecimalFormat df = new DecimalFormat("#.##");

  public static String prettify(float f) {
    return df.format(f);
  }

  public static String prettify(Vector4f v) {
    return "{ x: " + prettify(v.x) + ", y: " + prettify(v.y) + ", z: " + prettify(v.z) + ", w: " + prettify(
      v.w) + " }";
  }

  public static String prettify(Vector3f v) {
    return "{ x: " + prettify(v.x) + ", y: " + prettify(v.y) + ", z: " + prettify(v.z) + " }";
  }

  public static String prettify(Quaternionf q) {
    return "{ x: " + prettify(q.x) + ", y: " + prettify(q.y) + ", z: " + prettify(q.z) + ", w: " + prettify(
      q.w) + " }";
  }
}
