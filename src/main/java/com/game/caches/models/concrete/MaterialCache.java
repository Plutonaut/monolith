package com.game.caches.models.concrete;

import com.game.caches.models.AbstractModelCache;
import com.game.graphics.materials.Material;

public class MaterialCache extends AbstractModelCache {
  @Override
  protected Material generate(String key) {
    return new Material(key);
  }
}
