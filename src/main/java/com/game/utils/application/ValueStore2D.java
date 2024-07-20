package com.game.utils.application;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public class ValueStore2D {
  private final float[][] grid;
  private int width;
  private int height;
  private float min;
  private float max;

  public ValueStore2D(int width, int height) {
    this.width = width;
    this.height = height;
    this.min = Float.MAX_VALUE;
    this.max = Float.MIN_VALUE;

    grid = new float[height][width];
  }

  public float[] get(int row) {
    return grid[row];
  }

  public float get(int row, int column) {
    return grid[row][column];
  }

  public void set(int row, int column, float value) {
    grid[row][column] = value;

    if (value > max) max = value;
    if (value < min) min = value;
  }
}
