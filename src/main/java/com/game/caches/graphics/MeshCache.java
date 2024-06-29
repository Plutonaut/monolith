package com.game.caches.graphics;

import com.game.engine.render.mesh.Mesh;

public class MeshCache extends AbstractGraphicsCache {
  protected Mesh generate(String id) {
    return new Mesh(id);
  }
}
