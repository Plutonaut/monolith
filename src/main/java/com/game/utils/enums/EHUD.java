package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum EHUD {
  CONTROLS("controls"),
  ENTITY("entity"),
  STATUS("status"),
  FPS("fps");

  private final String value;

  EHUD(String value) {
    this.value = value;
  }
}
