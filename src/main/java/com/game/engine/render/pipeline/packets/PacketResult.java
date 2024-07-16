package com.game.engine.render.pipeline.packets;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;

public interface PacketResult {
  IRenderable create(Model model);
  void addMesh(Mesh mesh);
}
