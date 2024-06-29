package com.game.engine.scene.projection;

import com.game.engine.scene.camera.Camera;
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

  public Projection() {
    transform = new Matrix4f();

    fov = 60f;
    zNear = 0.01f;
    zFar = 1000f;
  }

  public Matrix4f modelView(Matrix4f model, Camera camera) {
    return transform.identity().set(camera.view()).mul(model);
  }

  public Matrix4f orthographic(Window window) {
    return transform.identity().ortho(0, window.width(), 0, window.height(), zNear, zFar);
  }

  public Matrix4f fontOrthographic(Window window) {
    return transform.identity().ortho2D(0, window.width(), window.height(), 0);
  }

  public Matrix4f perspective(Window window) {
    float aspectRatio = window.ratio();
    return transform.identity().perspective(fov, aspectRatio, zNear, zFar);
  }
}
