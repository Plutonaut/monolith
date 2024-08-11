package com.game.utils.application;

import org.joml.Random;

public class RandomNumberGenerator {
  private final Random random;

  public RandomNumberGenerator(int seed) {
    random = new Random(seed);
  }

  public float nextf() {
    return nextf(1);
  }

  public float nextf(float bounds) {
    return random.nextFloat() * bounds;
  }

  public int next(int bounds) {
    final int value = random.nextInt(bounds * 2);
    return value - bounds;
  }
}
