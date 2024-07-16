package com.game.caches.models;

import com.game.graphics.fonts.FontInfo;
import com.game.utils.engine.FontInfoUtils;

public class FontInfoCache extends AbstractModelCache {
  @Override
  protected FontInfo generate(String name) {
    return FontInfoUtils.process(name);
  }
}
