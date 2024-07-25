package com.game.caches.graphics;

import com.game.caches.graphics.interfaces.IGraphicsCachable;
import com.game.caches.graphics.interfaces.IGraphicsCache;
import com.game.caches.graphics.interfaces.IGraphicsGenerator;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractGraphicsCache implements IGraphicsCache {
  protected final ConcurrentHashMap<String, IGraphicsCachable> cache = new ConcurrentHashMap<>();

  @Deprecated
  public final void cache(IGraphicsCachable... entries) {
    for (IGraphicsCachable entry : entries) {
      if (entry != null && entry.key() != null)
        cache.put(entry.key(), entry);
    }
  }

  public IGraphicsCachable use(String key) {
    return use(key, this::generate);
  }

  public IGraphicsCachable use(String key, IGraphicsGenerator generator) {
    if (key == null) return null;
    return cache.computeIfAbsent(key, generator::generate);
//    IGraphicsCachable entry = cache.getOrDefault(key, generator.generate(key));
//    if (entry != null) cache.putIfAbsent(key, entry);
//    return entry;
  }

  protected abstract IGraphicsCachable generate(String key);

  public void dispose() {
    cache.values().forEach(IGraphicsCachable::dispose);
  }
}
