package com.game.caches.models.concrete;

import com.game.caches.models.AbstractModelCache;
import com.game.engine.render.mesh.MeshInfo;

public class MeshInfoCache extends AbstractModelCache {
  @Override
  protected MeshInfo generate(String name) {
    return new MeshInfo(name);
  }
}
