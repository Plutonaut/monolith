package com.game.engine.render.pipeline.packets;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityPacketResult implements PacketResult {
  private final List<Mesh> meshes;

  public EntityPacketResult() {
    meshes = new ArrayList<>();
  }

  @Override
  public Entity create(Model model) {
    String entityName = GlobalCache.instance().resolveEntityName(model.name());
    Entity entity = model.create(entityName);
    entity.addMeshes(meshes);
    return entity;
  }

  @Override
  public void addMesh(Mesh mesh) {
    this.meshes.add(mesh);
  }
}
