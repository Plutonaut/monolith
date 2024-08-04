package com.game.caches.models.concrete;

import com.game.caches.AbstractCache;
import com.game.engine.scene.sprites.SpriteAtlas;

public class SpriteAtlasCache extends AbstractCache {
  @Override
  protected SpriteAtlas generate(String path) {
    return SpriteAtlas.load(path);
  }
}
