package com.game.caches.graphics.concrete;

import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.engine.render.mesh.Mesh;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MeshCache extends AbstractGraphicsCache {
  protected Mesh generate(String id) {
    log.info("Generating mesh {}", id);
    return new Mesh(id);
  }
}
