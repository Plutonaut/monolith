package com.game.caches;

import com.game.graphics.IGraphicsCachable;

public interface ICacheUser {
  IGraphicsCachable use(String key);
}
