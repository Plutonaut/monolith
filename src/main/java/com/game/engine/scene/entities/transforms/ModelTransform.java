package com.game.engine.scene.entities.transforms;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Accessors(fluent = true)
@Data
public class ModelTransform {
  private final Matrix4f model;
  private final Vector3f position;
  private final Quaternionf rotation;
  private final Vector3f rotationV;
  public float scale;

  public ModelTransform() {
    model = new Matrix4f();
    position = new Vector3f();
    rotation = new Quaternionf();
    rotationV = new Vector3f();
    scale = 1f;
  }

  public Matrix4f worldModelMat() {
    return model.identity().translationRotateScale(position, rotation, scale);
  }

  public void set(ModelTransform transform) {
    this.position.set(transform.position);
    this.rotation.set(transform.rotation);
    this.rotationV.set(transform.rotationV);
    this.scale = transform.scale;
  }
}
