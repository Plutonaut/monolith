package com.game.caches.graphics;

import com.game.graphics.IGraphicsCachable;

import java.util.HashMap;

public abstract class AbstractGraphicsCache implements IGraphicsCache {
  protected final HashMap<String, IGraphicsCachable> cache = new HashMap<>();

  public final void cache(IGraphicsCachable... entries) {
    for (IGraphicsCachable entry : entries) {
      if (entry != null && entry.key() != null)
        cache.put(entry.key(), entry);
    }
  }

  public IGraphicsCachable use(String key) {
    if (key == null) return null;

    return cache.computeIfAbsent(key, this::generate);
  }

  public boolean has(String key) {
    return cache.containsKey(key);
  }

  protected abstract IGraphicsCachable generate(String key);

  public void dispose() {
    cache.values().forEach(IGraphicsCachable::dispose);
  }
}
