package com.game.utils.engine.terrain.procedural;

import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.scene.terrain.IHeightMapper;
import com.game.utils.application.values.ValueMap;
import com.game.utils.application.values.ValueStore;
import com.game.utils.engine.terrain.TerrainUtils;

public class ProceduralTerrainGeneratorUtils {
  public static void buildTerrainMeshInfo(
    ValueMap map, final MeshInfoBuilder builder, IHeightMapper mapper
  ) {
    String id = map.get("id");
    String diffuseTexturePath = map.get("diffuseTexturePath");
    String normalTexturePath = map.get("normalTexturePath");
    ValueStore positions = new ValueStore();
    ValueStore textureCoordinates = new ValueStore();
    ValueStore indices = new ValueStore();

    int width = map.getInt("width");
    int height = map.getInt("height");
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
