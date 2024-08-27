package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum EParticle {
  SPAWNER(0f),
  SHELL(1f),
  FRAG(2f);

  private final float value;

  EParticle(float value) {
    this.value = value;
  }
}
