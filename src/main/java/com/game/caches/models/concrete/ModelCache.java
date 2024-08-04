package com.game.caches.models.concrete;

import com.game.caches.AbstractCache;
import com.game.engine.render.models.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelCache extends AbstractCache {
  @Override
  protected Model generate(String name) {
    log.info("Generating model {}", name);
    return new Model(name);
  }
}
