package com.game.utils.engine.terrain.procedural;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.scene.terrain.IHeightMapper;
import com.game.engine.scene.terrain.procedural.ProceduralTerrainGenerationData;
import com.game.utils.application.LoaderUtils;
import com.game.utils.application.ValueStore;
import com.game.utils.application.ValueGrid;
import com.game.utils.engine.loaders.TextureLoader;
import com.game.utils.engine.terrain.TerrainUtils;

public class ProceduralTerrainGeneratorUtils {
  public static MeshInfo process(
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

  static MeshInfoBuilder safeMode_buildTerrainMeshInfo(ProceduralTerrainGenerationData data) {
    MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueGrid grid = new ValueGrid(data.width(), data.height());
    buildTerrainMeshInfo(data, builder, (int col, int row) -> {
      float height = 0.1f;
      grid.set(row, col, height);
      return height;
    });
    return builder;
  }

  // TODO: Attempt to pull from cache before loading texture from file.
  static MeshInfoBuilder buildTerrainMeshInfoFromTexture(
    ProceduralTerrainGenerationData data, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueGrid grid = new ValueGrid(data.width(), data.height());

    TextureLoader.read(
      heightMapTexturePath,
      (buffer, width, height) -> buildTerrainMeshInfo(
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

  static MeshInfoBuilder buildTerrainMeshInfoFromNoise(
    ProceduralTerrainGenerationData data, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    ValueGrid grid = ProceduralNoiseUtils.process(data.width(), data.height(), data.noise());
    GlobalCache.instance().texture(heightMapTexturePath, grid);
    buildTerrainMeshInfo(data, builder, grid::get);
    return builder;
  }

  static void buildTerrainMeshInfo(
    ProceduralTerrainGenerationData data, final MeshInfoBuilder builder, IHeightMapper mapper
  ) {
    String id = data.id();
    String diffuseTexturePath = data.textureMapData().diffuse();
    String normalTexturePath = data.textureMapData().normal();
    ValueStore positions = new ValueStore();
    ValueStore textureCoordinates = new ValueStore();
    ValueStore indices = new ValueStore();

    int width = data.width();
    int height = data.height();
    float xInc = TerrainUtils.incrementX(width);
    float zInc = TerrainUtils.incrementZ(height);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        float posX = TerrainUtils.X_AXIS + j * xInc;
        float posY = mapper.getHeight(j, i);
        float posZ = TerrainUtils.Z_AXIS + i * zInc;
        positions.add(posX, posY, posZ);

        float txcX = TerrainUtils.TEX_COORD_INC * j / width;
        float txcY = TerrainUtils.TEX_COORD_INC * i / height;
        textureCoordinates.add(txcX, txcY);

        if (j < width - 1 && i < height - 1) {
          int leftTop = i * width + j;
          int leftBottom = (i + 1) * width + j;
          int rightBottom = (i + 1) * width + j + 1;
          int rightTop = i * width + j + 1;

          indices.add(rightTop, leftBottom, rightBottom, leftTop, leftBottom, rightTop);
        }
      }
    }
    ValueStore normals = TerrainUtils.calculateNormals(positions, width, height);
    builder
      .use(id)
      .positions(positions)
      .textureCoordinates(textureCoordinates)
      .normals(normals)
      .indices(indices)
      .material(id + "_mat")
      .materialNormalTexture(normalTexturePath)
      .materialDiffuseTexture(diffuseTexturePath);
  }
}
