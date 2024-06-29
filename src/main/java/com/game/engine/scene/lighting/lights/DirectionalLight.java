package com.game.engine.scene.lighting.lights;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
public class DirectionalLight extends Light {
  protected final Vector3f direction;

  public DirectionalLight() {
    super();

    direction = new Vector3f(0f, 0f, -0.5f);
  }

  public float intensity() {
    return factor;
  }
}
