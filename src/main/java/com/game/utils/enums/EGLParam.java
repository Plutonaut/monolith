package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.stream.Stream;

@Accessors(fluent = true)
@Getter
public enum EGLParam {
  BLEND(0x1), DEPTH(0x2), CULL(0x8), WIREFRAME(0x16), DISCARD_RAST(0x32);

  private final int value;

  EGLParam(int value) { this.value = value; }

  public static int bitmap(EGLParam... params) {
    return Stream.of(params).mapToInt(EGLParam::value).reduce((u, v) -> u | v).orElse(0);
  }
}
