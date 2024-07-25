package com.game.caches.graphics.concrete;

import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.graphics.texture.Texture;
import com.game.utils.engine.loaders.TextureLoader;

public class TextureCache extends AbstractGraphicsCache {
  @Override
  protected Texture generate(String path) {
    return TextureLoader.load(path);
  }
}
