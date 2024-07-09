package com.game.caches;

import com.game.caches.graphics.IGraphicsCachable;

public interface ICacheUser {
  IGraphicsCachable use(String key);
}
