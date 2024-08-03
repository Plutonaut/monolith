package com.game.caches;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCache {
  protected final ConcurrentHashMap<String, ICachable> cache = new ConcurrentHashMap<>();

  public ICachable use(String key) {
    return use(key, this::generate);
  }

  public ICachable use(String key, ICachableGenerator generator) {
    if (key == null) return null;
    return cache.computeIfAbsent(key, generator::generate);
  }

  public ICachable cache(String key, ICachable item) {
    return (key == null || item == null) ? null : cache.put(key, item);
  }

  public ICachable remove(String key) {
    return key == null ? null : cache.remove(key);
  }

  protected abstract ICachable generate(String key);
}
