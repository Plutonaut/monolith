package com.game.caches.graphics;

import com.game.graphics.texture.Texture;
import com.game.utils.engine.TextureLoaderUtils;

public class TextureCache extends AbstractGraphicsCache {
  @Override
  protected Texture generate(String path) {
    return TextureLoaderUtils.load(path);
  }
}
