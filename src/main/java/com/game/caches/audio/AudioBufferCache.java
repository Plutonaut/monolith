package com.game.caches.audio;

import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.engine.scene.entities.animations.audio.AudioBufferObject;
import com.game.utils.engine.AudioUtils;

public class AudioBufferCache extends AbstractGraphicsCache {
  @Override
  protected AudioBufferObject generate(String key) {
    return AudioUtils.load(key);
  }
}
