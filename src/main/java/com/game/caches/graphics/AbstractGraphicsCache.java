package com.game.caches.graphics;

import com.game.caches.AbstractCache;
import com.game.caches.ICachable;
import com.game.graphics.IGraphics;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGraphicsCache extends AbstractCache {
  void handleDispose(String key, ICachable item) {
    if (item instanceof IGraphics graphics) {
      log.info("Disposing {} from cache", key);
      graphics.dispose();
    }
  }

  public void dispose() {
    cache.forEach(this::handleDispose);
  }
}
