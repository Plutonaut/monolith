package com.game.engine.scene.lighting.lights;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

@Accessors(fluent = true)
@Data
public class Light {
  protected final Vector3f color;
  protected float factor;

  public Light() {
    color = new Vector3f(1f);
    factor = 0.25f;
  }
}
