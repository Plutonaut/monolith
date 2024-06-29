package com.game.caches.graphics;

import com.game.graphics.shaders.Shader;

public class ShaderCache extends AbstractGraphicsCache {
  @Override
  protected Shader generate(String key) {
    if (key.isEmpty()) return null;

    return new Shader(key);
  }
}
