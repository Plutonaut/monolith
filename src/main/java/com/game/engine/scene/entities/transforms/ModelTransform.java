package com.game.engine.scene.entities.transforms;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Accessors(fluent = true)
@Data
public class ModelTransform {
  private final Matrix4f model;
  private final Vector3f position;
  private final Vector3f rotation;
  public float scale;

  public ModelTransform() {
    model = new Matrix4f();
    position = new Vector3f();
    rotation = new Vector3f();
    scale = 1f;
  }

  public Matrix4f worldModelMat() {
    return model.identity().translate(position)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
  }

  public void set(ModelTransform transform) {
    this.position.set(transform.position());
    this.rotation.set(transform.rotation());
    this.scale = transform.scale();
  }
}
