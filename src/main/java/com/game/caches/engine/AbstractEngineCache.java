package com.game.caches.engine;

import java.util.HashMap;

public abstract class AbstractEngineCache {
  protected final HashMap<String, ICachable> cache;

  public AbstractEngineCache() {
    cache = new HashMap<>();
  }

  public ICachable retrieve(String cacheKey) {
    return cache.getOrDefault(cacheKey, null);
  }

  public ICachable retrieveOrGenerate(String cacheKey, ICachableGenerator generator) {
    return cacheKey == null ? null : cache.computeIfAbsent(cacheKey, generator::generate);
  }
}
