package com.game.engine.scene.lighting.lights;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
public class SpotLight extends Light {
  protected final PointLight pointLight;
  protected final Vector3f coneDirection;
  protected float cutoff;

  public SpotLight() {
    pointLight = new PointLight();
    coneDirection = new Vector3f(-0.1f, 0f, -0.75f);
    cutoff = 0.5f;
  }

  public void pointConeToward(float x) {
    coneDirection.set(x);
  }

  public void pointConeToward(float x, float y, float z) {
    coneDirection.set(x, y, z);
  }
}
