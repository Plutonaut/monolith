package com.game.utils.engine.terrain.procedural;

import com.game.caches.GlobalCache;
import com.game.engine.physics.Bounds3D;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import com.game.utils.application.values.ValueGrid;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

@Accessors(fluent = true)
@Data
public class TerrainChunk {
  protected final TerrainChunkCoordinate coordinates;
  protected final MeshInfoBuilder builder;
  protected final Bounds3D bounds;
  protected final ValueGrid grid;
  protected final ArrayList<TerrainChunkCoordinate> neighbors;
  protected final Matrix4f terrainModel;

  public TerrainChunk(
    TerrainChunkCoordinate coordinates,
    ProceduralNoiseData data,
    float maxHeight,
    float minHeight,
    int size
  ) {
    float yPos = (maxHeight / 2f) + (minHeight / 2f);
    Vector3f origin = new Vector3f(coordinates.x() * size, yPos, coordinates.y() * size);
    this.grid = ProceduralNoiseUtils.process(data, size, size);
    this.coordinates = coordinates;
    int halfWidth = size / 2;
    int halfDepth = size / 2;
    bounds = new Bounds3D();
    bounds.min(new Vector3f(-halfWidth, minHeight, -halfDepth));
    bounds.max(new Vector3f(halfWidth, maxHeight, halfDepth));
    bounds.origin(origin);
    terrainModel = new Matrix4f();
    terrainModel.translate(origin).scale(size);
    this.builder = new MeshInfoBuilder();
    this.neighbors = new ArrayList<>();
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
    return GlobalCache.instance().meshInfo(
      coordinates.toString(),
      id -> ProceduralTerrainGeneratorUtils
        .buildTerrainMeshInfo(
          id,
          builder,
          Math.round(bounds().size().x),
          Math.round(bounds().size().z),
          grid()::get
        )
        .build()
    );
  }

  public boolean contains(Vector3f position) {
    return bounds.above(position);
  }
}
