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
  protected MeshInfo generateMeshInfo(
    ValueMap map
  ) {
    String heightMapTexturePath = map.get("heightMapTexturePath");
    String strategy = map.get("strategy");
    if (StringUtils.isEmpty(strategy)) {
      if (!StringUtils.isEmpty(heightMapTexturePath)) strategy = "texture";
      else strategy = "";
    }
    final MeshInfoBuilder builder = switch (strategy) {
      case "noise"
        -> buildTerrainMeshInfoFromNoise(map, heightMapTexturePath);
      case "texture" -> buildTerrainMeshInfoFromTexture(map, heightMapTexturePath);
      default -> safeMode_buildTerrainMeshInfo(map);
    };

    return builder.build();
  }

  MeshInfoBuilder safeMode_buildTerrainMeshInfo(ValueMap map) {
    MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueGrid grid = new ValueGrid(map.getInt("width"), map.getInt("height"));
    ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(map, builder, (int col, int row) -> {
      float height = 0.1f;
      grid.set(row, col, height);
      return height;
    });
    return builder;
  }

  // TODO: Attempt to pull from cache before loading texture from file.
  MeshInfoBuilder buildTerrainMeshInfoFromTexture(
    ValueMap map,
    String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueGrid grid = new ValueGrid(map.getInt("width"), map.getInt("height"));

    TextureLoader.read(
      heightMapTexturePath,
      (buffer, width, height) ->  ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(
        map,
        builder,
        (int col, int row) -> {
          float vertexHeight = TerrainUtils.getHeight(
            col,
            row,
            map.getFloat("minVertexHeight"),
            map.getFloat("maxVertexHeight"),
            width,
            buffer
          );
          grid.set(
            row,
            col,
            vertexHeight
          );
          return vertexHeight;
        }
      )
    );
    return builder;
  }

  MeshInfoBuilder buildTerrainMeshInfoFromNoise(
    ValueMap map, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    ValueGrid grid = ProceduralNoiseUtils.process(map);
    GlobalCache.instance().texture(heightMapTexturePath, grid);
    ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(map, builder, grid::get);
    return builder;
  }

  @Override
  public Model generateModel(ValueMap map) {
    String terrainId = map.get("id");
    MeshInfo info = GlobalCache
      .instance()
      .meshInfo(terrainId, id -> generateMeshInfo(map));
    Model model = new Model(terrainId);
    model.addMeshData(info.name());
    return model;
  }
}
