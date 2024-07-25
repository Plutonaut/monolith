package com.game.caches.models.concrete;

import com.game.caches.models.AbstractModelCache;
import com.game.graphics.materials.Material;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaterialCache extends AbstractModelCache {
  @Override
  protected Material generate(String key) {
    log.info("Generating material {}", key);
    return new Material(key);
  }
}
