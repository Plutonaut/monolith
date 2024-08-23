package com.game.utils.engine.terrain.procedural;

import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.scene.terrain.IHeightMapper;
import com.game.utils.application.values.ValueGrid;
import com.game.utils.application.values.ValueMap;
import com.game.utils.application.values.ValueStore;
import com.game.utils.engine.terrain.TerrainUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProceduralTerrainGeneratorUtils {
  public static void buildTerrainMeshInfo(
    ValueMap map, final MeshInfoBuilder builder, ValueGrid valueGrid
  ) {
    buildTerrainMeshInfo(map, builder, valueGrid.width(), valueGrid.height(), valueGrid::get);
  }

  public static void buildTerrainMeshInfo(
    ValueMap map, final MeshInfoBuilder builder, IHeightMapper mapper
  ) {
    buildTerrainMeshInfo(map, builder, map.getInt("width"), map.getInt("height"), mapper);
  }

  public static void buildTerrainMeshInfo(
    ValueMap map, final MeshInfoBuilder builder, int width, int height, IHeightMapper mapper
  ) {
    String id = map.get("id");
    String diffuseTexturePath = map.get("diffuseTexturePath");
    String normalTexturePath = map.get("normalTexturePath");
    String heightMapTexturePath = map.get("heightMapTexturePath");
    buildTerrainMeshInfo(id, builder, 1, width, height, mapper)
      .material(id + "_mat")
      .materialHeightTexture(heightMapTexturePath)
      .materialNormalTexture(normalTexturePath)
      .materialDiffuseTexture(diffuseTexturePath);
  }

  public static MeshInfoBuilder buildTerrainMeshInfo(
    String id,
    final MeshInfoBuilder builder,
    int lod,
    int width,
    int height,
    IHeightMapper mapper
  ) {
    ValueStore positions = new ValueStore();
    ValueStore textureCoordinates = new ValueStore();
    ValueStore indices = new ValueStore();

    int lodWidth = width / lod;
    int lodHeight = height / lod;

    float xInc = TerrainUtils.incrementX(lodWidth);
    float zInc = TerrainUtils.incrementZ(lodHeight);

    for (int i = 0; i < lodHeight; i++) {
      for (int j = 0; j < lodWidth; j++) {
        float posX = TerrainUtils.X_AXIS + j * xInc;
        float posY = mapper.getHeight(j * lod, i * lod);
        float posZ = TerrainUtils.Z_AXIS + i * zInc;

        positions.add(posX, posY, posZ);

        float txcX = TerrainUtils.TEX_COORD_INC * j / lodWidth;
        float txcY = TerrainUtils.TEX_COORD_INC * i / lodHeight;
        textureCoordinates.add(txcX, txcY);

        if (j < lodWidth - 1 && i < lodHeight - 1) {
          int bottomLeft = i * lodWidth + j;
          int topLeft = (i + 1) * lodWidth + j;
          int topRight = (i + 1) * lodWidth + j + 1;
          int bottomRight = i * lodWidth + j + 1;
          indices.add(topRight, bottomRight, bottomLeft, topRight, bottomLeft, topLeft);
        }
      }
    }
    ValueStore normals = TerrainUtils.calculateNormals(positions, lodWidth, lodHeight);

    return builder
      .use(id)
      .positions(positions)
      .textureCoordinates(textureCoordinates)
      .normals(normals)
      .indices(indices);
  }
}
