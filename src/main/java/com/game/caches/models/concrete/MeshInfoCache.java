package com.game.caches.models.concrete;

import com.game.caches.models.AbstractModelCache;
import com.game.engine.render.mesh.MeshInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MeshInfoCache extends AbstractModelCache {
  @Override
  protected MeshInfo generate(String name) {
    log.info("Generating mesh info {}", name);
    return new MeshInfo(name);
  }
}
