package com.game.caches.graphics;

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

    IGraphicsCachable entry = cache.getOrDefault(key, generate(key));
    if (entry != null) cache.putIfAbsent(key, entry);

    return entry;
  }

  protected abstract IGraphicsCachable generate(String key);

  public void dispose() {
    cache.values().forEach(IGraphicsCachable::dispose);
  }
}
