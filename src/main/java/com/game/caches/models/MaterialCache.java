package com.game.caches.models;

import com.game.graphics.materials.Material;

public class MaterialCache extends AbstractModelCache {
  @Override
  protected Material generate(String key) {
    return new Material(key);
  }
}
