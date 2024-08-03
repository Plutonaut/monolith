package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum EGLParam {
  BLEND(0x1),
  DEPTH(0x2),
  CULL(0x8),
  WIREFRAME(0x16);

  private final int value;

  EGLParam(int value) { this.value = value; }
}
