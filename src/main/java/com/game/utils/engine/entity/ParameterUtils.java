package com.game.utils.engine.entity;

import com.game.utils.enums.EGLParam;

import java.util.stream.Stream;

public class ParameterUtils {
  public static int toIntFlagValue(EGLParam... params) {
    return Stream.of(params).mapToInt(EGLParam::value).reduce((u, v) -> u | v).orElse(0);
  }
}
