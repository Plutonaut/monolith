package com.game.caches.models.concrete;

import com.game.caches.AbstractCache;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.engine.FontInfoUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FontInfoCache extends AbstractCache {
  // TODO: Add explicit generation callback parameter, remove reference to FontInfoUtils.
  @Override
  protected FontInfo generate(String name) {
    log.info("Generating font info {}", name);
    return FontInfoUtils.process(name);
  }
}
