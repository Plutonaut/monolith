package com.game.engine.scene.projection;

import com.game.engine.window.Window;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;

@Accessors(fluent = true)
@Data
public class Projection {
  private final Matrix4f transform;
  private float fov;
  private float zNear;
  private float zFar;

  public Projection(float fov, float zNear, float zFar) {
    transform = new Matrix4f();

    this.fov = (float) Math.toRadians(fov);
    this.zNear = zNear;
    this.zFar = zFar;
  }

  public Matrix4f orthographic(Window window) {
    return transform.identity().ortho(0, window.width(), 0, window.height(), zNear, zFar);
  }

  public Matrix4f orthographic2D(Window window) {
    return transform.identity().ortho2D(0, window.width(), 0, window.height());
  }

  public Matrix4f fontOrthographic2D(Window window) {
    return transform.identity().ortho2D(0, window.width(), window.height(), 0);
  }

  public Matrix4f centerOrthographic2D(Window window) {
    float halfWidth = window.width() / 2f;
    float halfHeight = window.height() / 2f;
    return transform.identity().ortho2D(-halfWidth, halfWidth, -halfHeight, halfHeight);
  }

  public Matrix4f perspective(Window window) {
    return transform.identity().perspective(fov, window.ratio(), zNear, zFar);
  }
}
