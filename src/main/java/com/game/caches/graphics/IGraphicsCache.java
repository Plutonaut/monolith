package com.game.caches.graphics;

import com.game.graphics.IGraphicsCachable;

public interface IGraphicsCache {
  void cache(IGraphicsCachable... entries);
}
