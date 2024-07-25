package com.game.caches.graphics.concrete;

import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.graphics.shaders.Program;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProgramCache extends AbstractGraphicsCache {
  @Override
  protected Program generate(String key) {
    log.info("Generating program {}", key);
    return new Program(key);
  }
}
