package com.game.engine.scene.camera;

import com.game.utils.logging.PrettifyUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Accessors(fluent = true)
@Getter
public class Camera {
  final ViewVectors vectors;
  final Matrix4f view;
  final Vector3f position;
  final Vector3f rotation;

  float mouseSensitivity;
  float cameraSpeed;

  public Camera(Vector3f position, Vector3f rotation, float mouseSensitivity, float cameraSpeed) {
    this.view = new Matrix4f();
    this.position = position;
    this.rotation = rotation;
    this.mouseSensitivity = mouseSensitivity;
    this.cameraSpeed = cameraSpeed;
    this.vectors = new ViewVectors(new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f());
  }

  public void move(float x, float y, float z) {
    x *= cameraSpeed;
    y *= cameraSpeed;
    z *= cameraSpeed;
    if (z != 0) {
      position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
      position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
    }

    if (x != 0) {
      position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
      position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
    }

    position.y += y;
  }

  public void rotate(float x, float y, float z) {
    x *= mouseSensitivity;
    y *= mouseSensitivity;
    z *= mouseSensitivity;

    rotation.add(new Vector3f(x, y, z));
  }

  public Vector4f worldPosition(Vector4f point) {
    return new Vector4f(point).mul(view3D().invert());
  }

  public ViewVectors viewVectors() {
    return vectors.update(view3D()).normalize();
  }

  public Matrix4f view3D() {
    return view(false);
  }

  public Matrix4f view2D() {
    return view(true);
  }

  Matrix4f view(boolean is2D) {
    Vector3f negatedPosition = position.negate(new Vector3f());
    view.identity();
    if (is2D) negatedPosition.z = 0;
    else view
      .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
      .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
    return view.translate(negatedPosition);
  }

  public record ViewVectors(Vector3f position, Vector3f forwards, Vector3f right, Vector3f up) {
    public ViewVectors update(Matrix4f view) {
      view.getColumn(0, right);
      view.getColumn(1, up);
      view.getColumn(2, forwards);
      view.getColumn(3, position);

      return this;
    }

    public ViewVectors normalize() {
      forwards.normalize();
      right.normalize();
      up.normalize();

      return this;
    }
  }

  @Override
  public String toString() {
    return "Camera" +
      "\nposition: " +
      PrettifyUtils.prettify(position) +
      "\nrotation: " +
      PrettifyUtils.prettify(rotation) +
      "\nsensitivity: " +
      PrettifyUtils.prettify(mouseSensitivity) +
      "\nspeed: " +
      PrettifyUtils.prettify(cameraSpeed);
  }
}
