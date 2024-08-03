package com.game.caches.audio;

import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.engine.scene.audio.AudioBufferObject;
import com.game.utils.engine.AudioUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AudioBufferCache extends AbstractGraphicsCache {
  @Override
  protected AudioBufferObject generate(String key) {
    log.info("Generating audio buffer at path {}", key);
    return AudioUtils.load(key);
  }
}
