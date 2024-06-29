package com.game.caches.graphics;

import com.game.graphics.shaders.Program;

public class ProgramCache extends AbstractGraphicsCache {
  @Override
  protected Program generate(String key) {
    return new Program(key);
  }
}
