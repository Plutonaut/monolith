package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum ELogic {
  TEST_PROC_GEN("testProcGen"),
  TEST_RENDER("testRender"),
  SAFE_MODE("safeMode");

  private final String value;

  ELogic(String value) {
    this.value = value;
  };
}
