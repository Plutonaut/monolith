package com.game.utils.application;

import com.game.graphics.texture.Texture;
import com.game.utils.application.values.ValueStore;
import org.joml.Random;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

public class RandomNumberGenerator {
  private final Random random;

  public RandomNumberGenerator(int seed) {
    random = new Random(seed);
  }

  public Vector3f nextv3() {
    float x = nextf();
    float y = nextf();
    float z = nextf();

    return new Vector3f(x, y, z);
  }

  public Vector3f nextv3(float min, float max) {
    float x = nextf(min, max);
    float y = nextf(min, max);
    float z = nextf(min, max);
    return new Vector3f(x, y, z);
  }

  public float nextf() {
    return random.nextFloat();
  }

  public float nextf(float bounds) {
    return random.nextFloat() * bounds;
  }

  public float nextf(float min, float max) {
    return min + nextf(max - min);
  }

  public int next(int bounds) {
    final int value = random.nextInt(bounds * 2);
    return value - bounds;
  }

  public Texture texture1D(int width) {
    Texture texture = new Texture("rand_1D_" + width, width, 0, GL46.GL_TEXTURE_1D);
    ValueStore values = new ValueStore();
    for (int i = 0; i < width; i++) {
      values.add(nextf());
      values.add(nextf());
      values.add(nextf());
    }
    texture.bind();
    texture.store();
    texture.upload1D(GL46.GL_RGB, GL46.GL_RGB, GL46.GL_FLOAT, values.asArray());
    texture.linear();
    texture.parameter(GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
    return texture;
  }
}
