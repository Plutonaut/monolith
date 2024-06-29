package com.game.utils.enums;

public enum EModifier {
  ANIMATED(EUniform.ANIMATED.value()),
  SELECTED(EUniform.SELECTED.value());

  private final String uniform;

  EModifier(String uniform) {
    this.uniform = uniform;
  }

  public String uniform() {
    return this.uniform;
  }
}
