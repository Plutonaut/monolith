package com.game.caches.models;

import com.game.caches.models.interfaces.IModelCachable;
import com.game.caches.models.interfaces.IModelCache;
import com.game.caches.models.interfaces.IModelGenerator;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractModelCache implements IModelCache {
  protected final ConcurrentHashMap<String, IModelCachable> cache = new ConcurrentHashMap<>();

  @Deprecated
  public final void cache(IModelCachable... models) {
    for (IModelCachable model : models) {
      if (model != null && model.name() != null)
        cache.put(model.name(), model);
    }
  }

  public IModelCachable use(String name) {
  // TODO: Replace with cache.getOrDefault(name, null);
    return use(name, this::generate);
  }

  public IModelCachable use(String name, IModelGenerator generator) {
    if (name == null) return null;
    return cache.computeIfAbsent(name, generator::generate);
//    IModelCachable model = cache.getOrDefault(name, generator.generate(name));
//    if (model != null) cache.putIfAbsent(name, model);
//    return model;
  }

  // TODO: Remove this
  protected abstract IModelCachable generate(String name);
}
