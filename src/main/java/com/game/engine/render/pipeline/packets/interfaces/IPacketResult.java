package com.game.engine.render.pipeline.packets.interfaces;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;

public interface IPacketResult {
  IRenderable create(Model model);
  void addMesh(Mesh mesh);
}
