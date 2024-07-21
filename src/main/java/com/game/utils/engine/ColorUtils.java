package com.game.utils.engine;

import com.game.utils.math.ScalarUtils;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIColor4D;

import java.awt.*;

public class ColorUtils {
  public static Vector4f convert(Color color) {
    float x = normalize(color.getRed());
    float y = normalize(color.getGreen());
    float z = normalize(color.getBlue());
    float w = normalize(color.getAlpha());

    return new Vector4f(x, y, z, w);
  }

  public static Vector4f convert(AIColor4D color) {
    float x = color.r();
    float y = color.g();
    float z = color.b();
    float w = color.a();

    return new Vector4f(x, y, z, w);
  }

  public static float normalize(int value) {return (float) value / 255f;}

  /**
   * Values above 1 and below 0 are clamped to decrease scope of interpolation.
   * Result is interpolated between 0 and 255, which are the base hex values for RGB.
   * @param value Value to clamp, interpolate, and round.
   * @return integer RGB value of resulting interpolated color value.
   */
  public static int interpolate(float value) {
    int interpolated = Math.round(ScalarUtils.clampedLerp01(0, 255, value));
    return new Color(interpolated, interpolated, interpolated).getRGB();
  }
}
