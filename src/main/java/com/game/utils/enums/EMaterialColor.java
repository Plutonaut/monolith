package com.game.utils.enums;

import org.lwjgl.assimp.Assimp;

import java.util.List;

public enum EMaterialColor {
  DIF(Assimp.AI_MATKEY_COLOR_DIFFUSE),
  SPC(Assimp.AI_MATKEY_COLOR_SPECULAR),
  AMB(Assimp.AI_MATKEY_COLOR_AMBIENT);

  private final String value;

  EMaterialColor(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static List<EMaterialColor> types() {
    return List.of(AMB, DIF, SPC);
  }
}
