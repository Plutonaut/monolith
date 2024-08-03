package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum EController {
  TEXT("text"),
  AUDIO("audio"),
  PHYS("physics"),
  BODY("physicsBody"),
  COLL("collider"),
  INSTANCE("instance"),
  ANIM("animation");

  private final String value;

  EController(String value) {this.value = value;}

}
