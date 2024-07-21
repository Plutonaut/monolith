package com.game.engine.scene.terrain.procedural;

import com.game.graphics.texture.TextureMapData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Data
public class ProceduralTerrainGenerationData {
  protected String id;
  protected int width;
  protected int height;
  protected float minVertexHeight;
  protected float maxVertexHeight;
  protected TextureMapData textureMapData;
  protected ProceduralNoiseData noise;
}
