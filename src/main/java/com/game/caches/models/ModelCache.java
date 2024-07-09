package com.game.caches.models;

import com.game.engine.render.models.Model;

public class ModelCache extends AbstractModelCache {
  @Override
  protected Model generate(String name) {
    return new Model(name);
  }
}
