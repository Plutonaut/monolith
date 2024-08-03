package com.game.utils.engine.entity;

import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import org.joml.Vector2f;

public class StaticEntityData {
  public static final ProceduralNoiseData PROCEDURAL_NOISE = new ProceduralNoiseData()
    .scale(1)
    .offset(new Vector2f())
    .seed(0)
    .octaves(1)
    .lacunarity(0.5f)
    .persistence(0.25f)
    .localNormalization(false);
  public static final String RESOURCE_DIRECTORY = "src/main/resources/";
}
