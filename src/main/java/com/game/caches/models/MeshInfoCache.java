package com.game.caches.models;

import com.game.engine.render.mesh.MeshInfo;

public class MeshInfoCache extends AbstractModelCache {
  @Override
  protected MeshInfo generate(String name) {
    return new MeshInfo(name);
  }
}
