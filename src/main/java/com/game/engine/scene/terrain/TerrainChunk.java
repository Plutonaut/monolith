package com.game.engine.scene.terrain;

import com.game.caches.GlobalCache;
import com.game.engine.physics.Bounds3D;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.utils.application.values.ValueGrid;
import com.game.utils.engine.terrain.procedural.ProceduralTerrainGeneratorUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashSet;

@Accessors(fluent = true)
@Slf4j
@Data
public class TerrainChunk {
  protected final TerrainChunkCoordinate coordinates;
  protected final MeshInfoBuilder builder;
  protected final Bounds3D bounds;
  protected final ValueGrid grid;
  protected final HashSet<TerrainChunkCoordinate> neighbors;
  protected final Matrix4f transform;
  protected int size;
  protected int lod;
  protected int lodIndex;

  public TerrainChunk(TerrainChunkCoordinate coordinates, int size) {
    this.coordinates = coordinates;
    this.builder = new MeshInfoBuilder();
    this.bounds = new Bounds3D();
    this.grid = new ValueGrid(size + 1, size + 1);
    this.neighbors = new HashSet<>();
    this.transform = new Matrix4f();
    this.size = size;
    this.lod = 0;

    generateChunkNeighbors();
  }

  public Matrix4f getWorldModel() {
    return transform
      .identity()
      .scale(size, 1, size)
      .translate(new Vector3f((float) coordinates.x(), 0, (float) coordinates.y()));
  }

  // Max LOD will be the lower value. LOD of 0 means highest possible level of detail
  // number of vertices = total area / LOD
  public int calculateLOD(Vector3f position, int maxLOD) {
    float distance = bounds.distanceXZ(position);
    // TODO: Change lod index calculation so that at least neighbors in the current chunk share the same lod
    int approximateLODIndex = Math.round(distance / (size * 2.5f));

    int previousLODIndex = lodIndex;
    int previousLOD = lod;
    // Clamping LOD index to be at least the maximum.
    lodIndex = Math.max(approximateLODIndex, maxLOD);
    lod = (int) Math.pow(
      2,
      lodIndex
    );
    if (previousLOD != lod || previousLODIndex != lodIndex) log.debug(
      "Terrain Chunk {} updated LOD {} to {} and LOD Index {} to {}",
      coordinates.toString(),
      previousLOD,
      lod,
      previousLODIndex,
      lodIndex
    );
    return lodIndex;
  }

  void calculateBounds(float minHeight, float maxHeight) {
    int halfSize = size / 2;
    Vector3f origin = new Vector3f(coordinates.x() * size, 0, coordinates.y() * size);
    bounds.min(new Vector3f(-halfSize, minHeight, -halfSize));
    bounds.max(new Vector3f(halfSize, maxHeight, halfSize));
    bounds.origin(origin);
  }

  void generateChunkNeighbors() {
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (i == 0 && j == 0) continue;
        TerrainChunkCoordinate coordinate = new TerrainChunkCoordinate(
          coordinates.x() + j,
          coordinates.y() + i
        );
        neighbors.add(coordinate);
      }
    }
  }

  public MeshInfo meshInfo() {
    return GlobalCache.instance().meshInfo(coordinates.toString(), this::generateMeshInfo);
  }

  public MeshInfo updateMeshInfo() {
    String id = coordinates.toString();
    return GlobalCache.instance().meshInfo(id, generateMeshInfo(id));
  }

  MeshInfo generateMeshInfo(String id) {
    return ProceduralTerrainGeneratorUtils.buildTerrainMeshInfo(
      id,
      builder,
      lod,
      size + 1,
      size + 1,
      grid()::get
    ).bounds(bounds).build();
  }

  public boolean contains(Vector3f position) {
    return bounds.above(position);
  }
}
