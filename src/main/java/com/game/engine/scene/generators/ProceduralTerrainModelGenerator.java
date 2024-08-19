package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.render.models.Model;
import com.game.utils.application.values.ValueGrid;
import com.game.utils.application.values.ValueMap;
import com.game.utils.engine.loaders.TextureLoader;
import com.game.utils.engine.terrain.TerrainUtils;
import com.game.utils.engine.terrain.procedural.ProceduralNoiseUtils;
import com.game.utils.engine.terrain.procedural.ProceduralTerrainGeneratorUtils;
import org.apache.commons.lang3.StringUtils;

public class ProceduralTerrainModelGenerator extends AbstractModelGenerator {
  @Override
  public Model generateModel(ValueMap map) {
    String terrainId = map.get("id");
    MeshInfo info = GlobalCache.instance().meshInfo(terrainId, id -> generateMeshInfo(map));
    Model model = new Model(terrainId);
    model.addMeshData(info.name());
    return model;
  }

  protected MeshInfo generateMeshInfo(
    ValueMap map
  ) {
    String heightMapTexturePath = map.get("heightMapTexturePath");
    String strategy = map.get("strategy");
    if (StringUtils.isEmpty(strategy)) {
      if (!StringUtils.isEmpty(heightMapTexturePath)) strategy = "texture";
      else strategy = "";
    }
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    switch (strategy) {
      case "noise" -> buildTerrainMeshInfoFromNoise(builder, map, heightMapTexturePath);
      case "texture" -> buildTerrainMeshInfoFromTexture(builder, map, heightMapTexturePath);
      default -> safeMode_buildTerrainMeshInfo(builder, map);
    }

    return builder.build();
  }

  void safeMode_buildTerrainMeshInfo(MeshInfoBuilder builder, ValueMap map) {
    ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(map, builder, (int col, int row) -> 0.1f);
  }

  void buildTerrainMeshInfoFromNoise(
    MeshInfoBuilder builder, ValueMap map, String heightMapTexturePath
  ) {
    ValueGrid grid = ProceduralNoiseUtils.process(map);
    GlobalCache.instance().texture(heightMapTexturePath, grid);
    ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(map, builder, grid);
  }

  // TODO: Attempt to pull from cache before loading texture from file.
  void buildTerrainMeshInfoFromTexture(
    MeshInfoBuilder builder, ValueMap map, String heightMapTexturePath
  ) {
    TextureLoader.read(
      heightMapTexturePath,
      (buffer, width, height) -> ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(
        map,
        builder,
        map.has("width") ? map.getInt("width") : width,
        map.has("height") ? map.getInt("height") : height,
        (int col, int row) -> TerrainUtils.getHeight(
          col,
          row,
          map.getFloat("minVertexHeight"),
          map.getFloat("maxVertexHeight"),
          width,
          buffer
        )
      )
    );
  }
}
