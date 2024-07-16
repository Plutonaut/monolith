package com.game.caches.models;

import com.game.engine.scene.sprites.SpriteAtlas;

public class SpriteAtlasCache extends AbstractModelCache{
  @Override
  protected SpriteAtlas generate(String path) {
    return SpriteAtlas.load(path);
  }
}
