package com.game.caches.graphics;

import com.game.graphics.IGraphics;
import com.game.utils.enums.EGraphicsCache;

public interface IGraphicsCachable extends IGraphics {
  EGraphicsCache type();
}
