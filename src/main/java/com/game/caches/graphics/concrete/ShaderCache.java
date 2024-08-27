package com.game.caches.graphics.concrete;

import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.graphics.shaders.Shader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShaderCache extends AbstractGraphicsCache {
  @Override
  protected Shader generate(String key) {
    if (key.isEmpty()) return null;

    log.debug("Generating shader {}", key);
    return new Shader(key);
  }
}
