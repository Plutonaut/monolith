package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.render.models.Model;
import com.game.engine.scene.generators.data.ProceduralTerrainGenerationData;
import com.game.utils.application.LoaderUtils;
import com.game.utils.application.ValueGrid;
import com.game.utils.engine.loaders.TextureLoader;
import com.game.utils.engine.terrain.TerrainUtils;
import com.game.utils.engine.terrain.procedural.ProceduralNoiseUtils;
import com.game.utils.engine.terrain.procedural.ProceduralTerrainGeneratorUtils;

public class ProceduralTerrainModelGenerator extends AbstractModelGenerator<ProceduralTerrainGenerationData> {
  protected MeshInfo generateMeshInfo(
    ProceduralTerrainGenerationData data
  ) {
    String heightMapTexturePath = data.textureMapData().height();
    final MeshInfoBuilder builder;
    if (LoaderUtils.isResourcePath(heightMapTexturePath))
      builder = buildTerrainMeshInfoFromTexture(data, heightMapTexturePath);
    else if (data.noise() != null)
      builder = buildTerrainMeshInfoFromNoise(data, heightMapTexturePath);
    else builder = safeMode_buildTerrainMeshInfo(data);
    return builder.build();
  }

  MeshInfoBuilder safeMode_buildTerrainMeshInfo(ProceduralTerrainGenerationData data) {
    MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueGrid grid = new ValueGrid(data.width(), data.height());
    ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(data, builder, (int col, int row) -> {
      float height = 0.1f;
      grid.set(row, col, height);
      return height;
    });
    return builder;
  }

  // TODO: Attempt to pull from cache before loading texture from file.
  MeshInfoBuilder buildTerrainMeshInfoFromTexture(
    ProceduralTerrainGenerationData data, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueGrid grid = new ValueGrid(data.width(), data.height());

    TextureLoader.read(
      heightMapTexturePath,
      (buffer, width, height) ->  ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(
        data,
        builder,
        (int col, int row) -> {
          float vertexHeight = TerrainUtils.getHeight(
            col,
            row,
            data.minVertexHeight(),
            data.maxVertexHeight(),
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
    ProceduralTerrainGenerationData data, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    ValueGrid grid = ProceduralNoiseUtils.process(data.width(), data.height(), data.noise());
    GlobalCache.instance().texture(heightMapTexturePath, grid);
    ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(data, builder, grid::get);
    return builder;
  }

  @Override
  public Model generateModel(ProceduralTerrainGenerationData data) {
    MeshInfo info = GlobalCache
      .instance()
      .meshInfo(data.id(), id -> generateMeshInfo(data));
    Model model = new Model(data.id());
    model.addMeshData(info.name());
    return model;
  }
}
