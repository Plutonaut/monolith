package com.game.caches.models.concrete;

import com.game.caches.models.AbstractModelCache;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.engine.FontInfoUtils;

public class FontInfoCache extends AbstractModelCache {
  // TODO: Add explicit generation callback parameter, remove reference to FontInfoUtils.
  @Override
  protected FontInfo generate(String name) {
    return FontInfoUtils.process(name);
  }
}
