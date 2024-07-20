package com.game.utils.engine.terrain.procedural;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.scene.terrain.IHeightMapper;
import com.game.engine.scene.terrain.procedural.ProceduralTerrainGenerationData;
import com.game.graphics.texture.Texture;
import com.game.utils.application.LoaderUtils;
import com.game.utils.application.ValueStore;
import com.game.utils.application.ValueStore2D;
import com.game.utils.engine.TextureLoaderUtils;
import com.game.utils.engine.terrain.TerrainUtils;
import org.lwjgl.opengl.GL46;

public class ProceduralTerrainGeneratorUtils {
  public static MeshInfo process(
    ProceduralTerrainGenerationData data
  ) {
    String heightMapTexturePath = data.heightMapTexture();
    final MeshInfoBuilder builder = LoaderUtils.isResourcePath(heightMapTexturePath)
                                    ? buildTerrainMeshInfoFromTexture(data, heightMapTexturePath)
                                    : buildTerrainMeshInfoFromNoise(data, heightMapTexturePath);
    return builder.build();
  }

  static MeshInfoBuilder buildTerrainMeshInfoFromTexture(
    ProceduralTerrainGenerationData data, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueStore2D grid = new ValueStore2D(data.width(), data.height());
    Texture texture = new Texture(heightMapTexturePath);
    texture.bind();
    texture.store();
    texture.clamp();
    texture.mipmap();

    String fileType = LoaderUtils.getFileType(heightMapTexturePath);
    int format = fileType != null ? TextureLoaderUtils.formatByFileType(fileType) : GL46.GL_RGBA;
    TextureLoaderUtils.readTexture(heightMapTexturePath, (((buffer, w, h) -> {
      texture.width(w);
      texture.height(h);
      texture.upload(GL46.GL_RGBA, format, GL46.GL_UNSIGNED_BYTE, buffer);
      buildTerrainMeshInfo(data, builder, (int row, int col) -> {
        float vertexHeight = TerrainUtils.getHeight(
          col,
          row,
          data.minVertexHeight(),
          data.maxVertexHeight(),
          data.width(),
          buffer
        );
        grid.set(row, col, vertexHeight);
        return vertexHeight;
      });
    })));
    GlobalCache.instance().cacheItem(texture);
    return builder;
  }

  static MeshInfoBuilder buildTerrainMeshInfoFromNoise(
    ProceduralTerrainGenerationData data, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    ValueStore2D grid = ProceduralNoiseUtils.process(data.width(), data.height(), data.noise());
    Texture texture = TextureLoaderUtils.generate(heightMapTexturePath, grid, true);
    buildTerrainMeshInfo(data, builder, grid::get);
    GlobalCache.instance().cacheItem(texture);
    return builder;
  }

  static void buildTerrainMeshInfo(
    ProceduralTerrainGenerationData data, final MeshInfoBuilder builder, IHeightMapper mapper
  ) {
    String id = data.id();
    String diffuseTexturePath = data.diffuseTexture();
    String heightMapTexturePath = data.heightMapTexture();
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
    builder.use(id)
           .positions(positions)
           .textureCoordinates(textureCoordinates)
           .normals(normals)
           .indices(indices)
           .material(id + "_mat")
           .materialDiffuseTexture(diffuseTexturePath)
           .materialHeightTexture(heightMapTexturePath);
  }
}
