package com.game.engine.render.mesh;

import com.game.engine.render.mesh.vertices.VertexInfo;

public interface IVertexCallback {
  void onComplete(VertexInfo info);
}
