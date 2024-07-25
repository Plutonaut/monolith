package com.game.caches.models.concrete;

import com.game.caches.models.AbstractModelCache;
import com.game.engine.render.models.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelCache extends AbstractModelCache {
  @Override
  protected Model generate(String name) {
    log.info("Generating model {}", name);
    return new Model(name);
  }
}
