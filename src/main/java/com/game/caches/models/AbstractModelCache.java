package com.game.caches.models;

import java.util.HashMap;

public abstract class AbstractModelCache implements IModelCache {
  protected final HashMap<String, IModelCachable> cache = new HashMap<>();

  public final void cache(IModelCachable... models) {
    for (IModelCachable model : models) {
      if (model != null && model.name() != null)
        cache.put(model.name(), model);
    }
  }

  public IModelCachable use(String name) {
    return use(name, this::generate);
  }

  public IModelCachable use(String name, IModelGenerator generator) {
    if (name == null) return null;

    IModelCachable model = cache.getOrDefault(name, generator.generate(name));
    if (model != null) cache.putIfAbsent(name, model);
    return model;
  }

  protected abstract IModelCachable generate(String name);
}
