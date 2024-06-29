package com.game.engine.scene.lighting.lights;

import com.game.engine.scene.lighting.Attenuation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
public class PointLight extends Light {
  protected final Vector3f position;
  protected Attenuation attenuation;

  public PointLight() {
    super();

    position = new Vector3f(0f, 0f, -1.4f);
    attenuation = new Attenuation();
  }

  public void move(float x) {
    position.set(x);
  }

  public void move(float x, float y, float z) {
    position.set(x, y, z);
  }

  public float intensity() {
    return factor;
  }

  public void intensity(float intensity) {
    factor = intensity;
  }
}
