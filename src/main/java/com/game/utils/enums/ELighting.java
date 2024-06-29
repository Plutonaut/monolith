package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum ELighting {
  AMBIENT("ambient"),
  DIRECTIONAL("directional"),
  POINT("point."),
  SPOT("spot.");

  private final String value;

  ELighting(String value) {
    this.value = value;
  }

  public boolean isType(String key) {
    return key.startsWith(value);
  }

  public String prefix(String key) {
    if (isType(key)) return key;

    if(key.contains(value)) key = key.replace(value, "");
    return value + key;
  }
}
