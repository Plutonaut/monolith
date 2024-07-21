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
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
    final ValueStore2D grid = new ValueStore2D(data.width(), data.height());
    buildTerrainMeshInfo(data, builder, (int col, int row) -> {
      float height = 0.1f;
      grid.set(row, col, height);
      return height;
    });
    return builder;
  }

  static MeshInfoBuilder buildTerrainMeshInfoFromTexture(
    ProceduralTerrainGenerationData data, String heightMapTexturePath
  ) {
    final MeshInfoBuilder builder = new MeshInfoBuilder();
    final ValueStore2D grid = new ValueStore2D(data.width(), data.height());
    Texture texture;

    String fileType = LoaderUtils.getFileType(heightMapTexturePath);
    int format = TextureLoaderUtils.formatByFileType(fileType);
    ByteBuffer buffer;

    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer comp = stack.mallocInt(1);

      STBImage.stbi_set_flip_vertically_on_load(true);
      buffer = STBImage.stbi_load(heightMapTexturePath, w, h, comp, 4);

      if (buffer == null)
        throw new RuntimeException("Texture path: " + heightMapTexturePath + System.lineSeparator() + STBImage.stbi_failure_reason());

      int width = w.get();
      int height = h.get();
      texture = new Texture(heightMapTexturePath);
      texture.width(width);
      texture.height(height);
      texture.bind();
      texture.store();
      texture.filter();
      texture.upload(GL46.GL_RGBA, format, GL46.GL_UNSIGNED_BYTE, buffer);
      buildTerrainMeshInfo(data, builder, (int col, int row) -> {
        float vertexHeight = TerrainUtils.getHeight(col,
                                                    row,
                                                    data.minVertexHeight(),
                                                    data.maxVertexHeight(),
                                                    width,
                                                    buffer
        );
        grid.set(row, col, vertexHeight);
        return vertexHeight;

      });
      STBImage.stbi_image_free(buffer);
    }
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
    String diffuseTexturePath = data.textureMapData().diffuse();
    String normalTexturePath = data.textureMapData().normal();
    String heightMapTexturePath = data.textureMapData().height();
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
      .materialDiffuseTexture(diffuseTexturePath)
      .materialHeightTexture(heightMapTexturePath);
  }
}
