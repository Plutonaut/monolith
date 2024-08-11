package com.game.utils.engine.terrain.procedural;

import com.game.utils.application.RandomNumberGenerator;
import com.game.utils.application.values.ValueGrid;
import com.game.utils.application.values.ValueMap;
import org.joml.SimplexNoise;
import org.joml.Vector2f;

public class ProceduralNoiseUtils {
  public static final int OCTAVE_BOUNDS = 100000;
  public static final int DFLT_CHUNK_SIZE = 239;
  public static final int SHARP_FALL_OFF_A = 3;
  public static final float SHARP_FALL_OFF_B = 2.2f;

  public static ValueGrid process(ValueMap map) {
    int width = map.getInt("width");
    int height = map.getInt("height");
    int octaves = map.getInt("octaves");
    Vector2f offset = map.getVector2f("offset");
    Vector2f[] octaveOffsets = new Vector2f[octaves];
    RandomNumberGenerator rng = new RandomNumberGenerator(map.getInt("seed"));

    // Multiplied against output to control range of values by clamping them.
    float amplitude = 1f;
//    float maxHeight = 0f;

    for (int i = 0; i < octaves; i++) {
      float x = rng.next(OCTAVE_BOUNDS) + offset.x;
      float y = rng.next(OCTAVE_BOUNDS) - offset.y;

      octaveOffsets[i] = new Vector2f(x, y);
//      maxHeight += amplitude;
      amplitude *= map.getFloat("persistence");
    }

    ValueGrid grid = new ValueGrid(width, height);
    float scale = map.getFloat("scale");
    float persistence = map.getFloat("persistence");
    float lacunarity = map.getFloat("lacunarity");

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
        grid.set(y, x, noiseHeight);
      }
    }

    return grid;
  }
}
