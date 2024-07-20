package com.game.engine.scene.terrain.procedural;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.models.Model;
import com.game.utils.engine.terrain.procedural.ProceduralTerrainGeneratorUtils;

// TODO: Bundle with other loader classes.
public class ProceduralTerrainGenerator {
  public static Model generate(ProceduralTerrainGenerationData data) {
    return GlobalCache.instance().model(data.id(), id -> generateModel(data));
  }

  static Model generateModel(ProceduralTerrainGenerationData data) {
    MeshInfo info = GlobalCache
      .instance()
      .meshInfo(data.id(), id -> ProceduralTerrainGeneratorUtils.process(data));
    Model model = new Model(data.id());
    model.addMeshData(info.name());
    return model;
  }
}
