package com.game.utils.math;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class VectorUtils {
  public static Vector2f screenSpace(Vector3f point, int width, int height) {
    float x = (point.x + point.x * 0.5f) * width;
    float y = (point.y - point.y * 0.5f) * height;
    return new Vector2f(x, y);
  }

  public static Vector4f viewSpace(Matrix4f matrix, Vector3f point) {
    return viewSpace(matrix, new Vector4f(point, 1.0f));
  }

  public static Vector4f viewSpace(Matrix4f matrix, Vector4f point) {
    Vector4f viewPoint = new Vector4f(point).mul(matrix.invert());

    viewPoint.z = -1.0f;
    viewPoint.w = 0.0f;

    return viewPoint;
  }
}
