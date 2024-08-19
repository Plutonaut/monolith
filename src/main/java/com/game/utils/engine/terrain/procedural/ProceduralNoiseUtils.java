package com.game.utils.engine.terrain.procedural;

import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import com.game.utils.application.RandomNumberGenerator;
import com.game.utils.application.values.ValueGrid;
import com.game.utils.application.values.ValueMap;
import com.game.utils.math.ScalarUtils;
import org.joml.SimplexNoise;
import org.joml.Vector2f;

public class ProceduralNoiseUtils {
  public static final int OCTAVE_BOUNDS = 100000;
  public static final int DFLT_CHUNK_SIZE = 239;
  public static final int SHARP_FALL_OFF_A = 3;
  public static final float SHARP_FALL_OFF_B = 2.2f;

  public static ValueGrid process(ValueMap map) {
    Vector2f offset = map.getVector2f("offset");
    int width = map.getInt("width");
    int height = map.getInt("height");
    int octaves = map.getInt("octaves");
    int seed = map.getInt("seed");
    float persistence = map.getFloat("persistence");
    float scale = map.getFloat("scale");
    float lacunarity = map.getFloat("lacunarity");
    float minHeight = map.getFloat("minVertexHeight");
    float maxHeight = map.getFloat("maxVertexHeight");

    return process(
      offset,
      width,
      height,
      minHeight,
      maxHeight,
      octaves,
      seed,
      persistence,
      scale,
      lacunarity
    );
  }

  public static ValueGrid process(ProceduralNoiseData data, int width, int height) {
    Vector2f offset = data.offset();
    int octaves = data.octaves();
    int seed = data.seed();
    float persistence = data.persistence();
    float scale = data.scale();
    float lacunarity = data.lacunarity();

    return process(offset, width, height, 0f, 0.01f, octaves, seed, persistence, scale, lacunarity);
  }

  public static ValueGrid process(
    Vector2f offset,
    int width,
    int height,
    float minVertexHeight,
    float maxVertexHeight,
    int octaves,
    int seed,
    float persistence,
    float scale,
    float lacunarity
  ) {
    Vector2f[] octaveOffsets = new Vector2f[octaves];
    RandomNumberGenerator rng = new RandomNumberGenerator(seed);

    // Multiplied against output to control range of values by clamping them.
    float amplitude = 1f;
//    float maxHeight = 0f;

    for (int i = 0; i < octaves; i++) {
      float x = rng.next(OCTAVE_BOUNDS) + offset.x;
      float y = rng.next(OCTAVE_BOUNDS) - offset.y;

      octaveOffsets[i] = new Vector2f(x, y);
//      maxHeight += amplitude;
      amplitude *= persistence;
    }

    ValueGrid grid = new ValueGrid(width, height);
    float halfWidth = width / 2f;
    float halfHeight = height / 2f;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        amplitude = 1f;
        float frequency = 1f;
        float noiseHeight = 0f;

        for (int i = 0; i < octaves; i++) {
          float scaleFrequency = scale * frequency;
          float scaleX = x - halfWidth + octaveOffsets[i].x / scaleFrequency;
          float scaleY = y - halfHeight + octaveOffsets[i].y / scaleFrequency;

          float noiseValue = SimplexNoise.noise(scaleX, scaleY);
          float value = (noiseValue + 1) * 0.5f;
          noiseHeight += value * amplitude;

          amplitude *= persistence;
          frequency *= lacunarity;
        }
        noiseHeight = ScalarUtils.lerp(minVertexHeight, maxVertexHeight, noiseHeight);
        grid.set(y, x, noiseHeight);
      }
    }

    return grid;
  }
}
