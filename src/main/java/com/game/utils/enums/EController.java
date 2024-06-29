package com.game.utils.enums;

public enum EController {
  AUDIO("audio"),
  BODY("physicsBody"),
  COLL("collider"),
  ANIM("animation");

  private final String value;

  EController(String value) {this.value = value;}

  public String getValue() {return value;}
}
