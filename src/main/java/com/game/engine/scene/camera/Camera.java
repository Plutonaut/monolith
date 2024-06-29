package com.game.engine.scene.camera;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Accessors(fluent = true)
@Getter
public class Camera {
  final Vector3f position;
  final Vector3f rotation;

  float mouseSensitivity;
  float cameraSpeed;

  public Camera(Vector3f position, Vector3f rotation, float mouseSensitivity, float cameraSpeed) {
    this.position = position;
    this.rotation = rotation;

    this.mouseSensitivity = mouseSensitivity;
    this.cameraSpeed = cameraSpeed;
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
    return new Vector4f(point).mul(view().invert());
  }

  public Matrix4f view() {
    return new Matrix4f().identity()
                         .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                         .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                         .translate(position.negate(new Vector3f()));
  }
}
