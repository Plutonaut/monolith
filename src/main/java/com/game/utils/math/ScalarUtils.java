package com.game.utils.math;

public class ScalarUtils {
  /**
   * @param v Value to be clamped.
   * @return The result of v clamped between 0 and 1
   */
  public static float clamp01(float v) {
    return Math.clamp(v, 0, 1);
  }

  /**
   * @param a Left most interpolation value.
   * @param b Right most interpolation value.
   * @param v Value to be clamped and interpolated
   * @return Result of value v clamped between 0 and 1 and interpolated between a and b.
   */
  public static float clampedLerp01(float a, float b, float v) {
    return lerp(a, b, clamp01(v));
  }

  /**
   * @param a Left most interpolation value.
   * @param b Right most interpolation value.
   * @param t Value to be interpolated.
   * @return Result of value t interpolated between and b
   */
  public static float lerp(float a, float b, float t) {
    return (1.0f - t) * a + b * t;
  }

  public static float invLerp(float a, float b, float v) {
    return (v - a) / (b - a);
  }

  public static float smoothStep(float min, float max, float value) {
    final float x = invLerp(min, max, value);
    return x * x * (3.0f - 2.0f * x);
  }

  public static float globalNormalization(float value, float maxHeight) {
    return Math.max((value + 1) / (maxHeight / 0.9f), 0);
  }
}
