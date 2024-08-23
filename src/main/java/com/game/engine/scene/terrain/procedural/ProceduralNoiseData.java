package com.game.engine.scene.terrain.procedural;

import com.game.utils.application.values.ValueMap;
import com.game.utils.math.ScalarUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joml.Vector2f;

/**
 *
 */
@Accessors(fluent = true)
@NoArgsConstructor
@Data
public class ProceduralNoiseData {
  protected Vector2f offset;
  protected float persistence;
  protected float lacunarity;
  protected float scale;
  protected int octaves;
  protected int seed;
  protected boolean localNormalization;

  /**
   * @param offset
   *   Position value which dictates where in the fractal pattern the sample takes place.
   * @param persistence
   *   Value between 0 and 1 which controls the decrease in amplitude of each successive octave,
   *   affecting the "roughness" of the final output.
   * @param lacunarity
   *   Value with a max of 1 which acts as a multiplier that determines the rate of change in scale
   *   between each octave.
   * @param scale
   *   Value above 0 which controls the scale of the noise function in the x, y, and z directions.
   * @param octaves
   *   The number of frequencies that the fractal noise is repeated over. Each octave is twice the
   *   frequency. High impact on performance.
   * @param seed
   *   Value of random seed used when generating random values in noise algorithm.
   * @param localNormalization
   *   Flag which controls whether normalization is applied relative to neighboring fractals.
   */
  public ProceduralNoiseData(
    Vector2f offset,
    float persistence,
    float lacunarity,
    float scale,
    int octaves,
    int seed,
    boolean localNormalization
  ) {
    this.offset = offset;
    this.persistence = ScalarUtils.clamp01(persistence);
    this.lacunarity = Math.max(lacunarity, 1);
    this.scale = Math.max(scale, 0.01f);
    this.octaves = octaves;
    this.seed = seed;
    this.localNormalization = localNormalization;
  }

  public static ProceduralNoiseData createFromMap(ValueMap map) {
    Vector2f offset = map.getVector2f("offset");
    float persistence = map.getFloat("persistence");
    float lacunarity = map.getFloat("lacunarity");
    float scale = map.getFloat("scale");
    int octaves = map.getInt("octaves");
    int seed = map.getInt("seed");
    boolean localNormalization = map.getBool("localNormalization");

    return new ProceduralNoiseData(
      offset,
      persistence,
      lacunarity,
      scale,
      octaves,
      seed,
      localNormalization
    );
  }
}
