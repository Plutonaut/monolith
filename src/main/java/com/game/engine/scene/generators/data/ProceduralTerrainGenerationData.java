package com.game.engine.scene.generators.data;

import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import com.game.graphics.texture.TextureMapData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Data
public class ProceduralTerrainGenerationData extends AbstractGenerationData {
  protected int width;
  protected int height;
  protected float minVertexHeight;
  protected float maxVertexHeight;
  protected TextureMapData textureMapData;
  protected ProceduralNoiseData noise;

  public ProceduralTerrainGenerationData(
    String id,
    int width,
    int height,
    float minVertexHeight,
    float maxVertexHeight,
    TextureMapData textureMapData,
    ProceduralNoiseData noise
  ) {
    super(id);
    this.width = width;
    this.height = height;
    this.minVertexHeight = minVertexHeight;
    this.maxVertexHeight = maxVertexHeight;
    this.textureMapData = textureMapData;
    this.noise = noise;
  }
}
