package com.game.utils.application;

import org.joml.Random;

public class RandomNumberGenerator {
  private final Random random;

  public RandomNumberGenerator(int seed) {
    random = new Random(seed);
  }

  public float next(float bounds) {
    final float max = bounds * 2;
    final float value = random.nextFloat() * max;

    return value - bounds;
  }

  public int next(int bounds) {
    final int value = random.nextInt(bounds * 2);
    return value - bounds;
  }
}
