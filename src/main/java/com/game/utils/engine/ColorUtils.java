package com.game.utils.engine;

import com.game.utils.math.ScalarUtils;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIColor4D;

import java.awt.*;

public class ColorUtils {
  public static Vector4f convert(Color color) {
    float x = normalizei(color.getRed());
    float y = normalizei(color.getGreen());
    float z = normalizei(color.getBlue());
    float w = normalizei(color.getAlpha());

    return new Vector4f(x, y, z, w);
  }

  public static Vector4f convert(AIColor4D color) {
    float x = color.r();
    float y = color.g();
    float z = color.b();
    float w = color.a();

    return new Vector4f(x, y, z, w);
  }

  public static Vector4f normalize(Vector3f value) {
    float x = normalize(value.x);
    float y = normalize(value.y);
    float z = normalize(value.z);

    return new Vector4f(x, y, z, 1);
  }

  public static float normalizei(int value) { return normalize((float) value); }

  public static float normalize(float value) { return value / 255f; }

  /**
   * Values above 1 and below 0 are clamped to decrease scope of interpolation. Result is
   * interpolated between 0 and 255, which are the base hex values for RGB.
   *
   * @param value
   *   Value to clamp, interpolate, and round.
   *
   * @return integer RGB value of resulting interpolated color value.
   */
  public static int interpolate(float value) {
    int interpolated = Math.round(ScalarUtils.clampedLerp01(0, 255, value));
    return new Color(interpolated, interpolated, interpolated).getRGB();
  }
}
