package com.game.engine.scene.lighting;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public class Attenuation {
  private float constant;
  private float linear;
  private float exponent;

  public Attenuation() {
    constant = 0f;
    linear = 0f;
    exponent = 1f;
  }
}
