package com.game.utils.engine.terrain.procedural;

import org.joml.Vector3f;

public class TerrainChunkUtils {
  public static final String PROC_TERRAIN_PATH = "properties/ini/proc_terrain_1.ini";

  public static int compareChunks(TerrainChunk a, TerrainChunk b) {
    TerrainChunkCoordinate aCoordinates = a.coordinates();
    TerrainChunkCoordinate bCoordinates = b.coordinates();

    if (aCoordinates.isCoordinate(bCoordinates.x(), bCoordinates.y())) return 0;
    if (aCoordinates.isGreaterThan(bCoordinates.x(), bCoordinates.y())) return 1;
    return -1;
  }

  public static TerrainChunkCoordinate convertPositionToCoordinates(Vector3f position, int size) {
    int coordinateX = (Math.round(position.x() / (float) size));
    int coordinateY = (Math.round(position.z() / (float) size));
    return new TerrainChunkCoordinate(coordinateX, coordinateY);
  }
}
