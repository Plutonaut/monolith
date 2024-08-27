package com.game.engine.scene.terrain;

public record TerrainChunkCoordinate(int x, int y) {
  public boolean isCoordinate(int x, int y) {
    return this.x == x && this.y == y;
  }

  public boolean isGreaterThan(int x, int y) {
    return this.x > x || this.x == x && this.y > y;
  }

  @Override
  public String toString() {
    return "terrain_chunk_(%d, %d)".formatted(x, y);
  }
}
