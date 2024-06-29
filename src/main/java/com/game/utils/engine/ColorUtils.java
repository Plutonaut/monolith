package com.game.utils.engine;

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
}
