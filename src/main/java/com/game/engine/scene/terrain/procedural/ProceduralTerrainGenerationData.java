package com.game.engine.scene.terrain.procedural;

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
  protected String diffuseTexture;
  protected String heightMapTexture;
  protected ProceduralNoiseData noise;
}
