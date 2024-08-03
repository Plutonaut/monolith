package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum EAudio {
  CREAK("entity/creak1.ogg"),
  MUSIC_WOO("music/woo_scary.ogg");

  private final String value;

  EAudio(String value) { this.value = value; }
}
