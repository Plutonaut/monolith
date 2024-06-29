package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

public enum EAnimationState {
  PLAYING(1),
  REVERSE(-1),
  STOPPED(0);

  @Accessors(fluent = true)
  @Getter
  private final int speed;

  EAnimationState(int speed) {
    this.speed = speed;
  }
}
