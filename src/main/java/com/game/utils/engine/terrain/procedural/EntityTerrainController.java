package com.game.utils.engine.terrain.procedural;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.scene.entities.controllers.AbstractEntityController;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.utils.application.LambdaCounter;
import com.game.utils.application.values.ValueMap;
import com.game.utils.enums.EController;
import com.game.utils.enums.EMaterialTexture;
import com.game.utils.enums.ERenderer;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;

@Accessors(fluent = true)
@Slf4j
public class EntityTerrainController extends AbstractEntityController {
  static final int MIN_CHUNK_SIZE = 20;
  @Getter
  protected final HashMap<Integer, TerrainChunk> active;

  protected final HashMap<TerrainChunkCoordinate, TerrainChunk> chunks;
  protected final Program program;
  protected ProceduralNoiseData data;
  protected Material material;
  protected float maxHeight;
  protected float minHeight;
  protected int size;

  TerrainChunk current;

  public EntityTerrainController() {
    chunks = new HashMap<>();
    active = new HashMap<>();
    program = GlobalCache.instance().program(ERenderer.TERRAIN.key());
  }

  @Override
  public String type() {
    return EController.TERRAIN.value();
  }

  @Override
  public void onUpdate(ModelTransform transform) {
  }

  public void generate(ValueMap map) {
    float maxViewDistance = map.getFloat("maxViewDistance");
    int roundedFromCenter = Math.round(maxViewDistance / 1.5f);
    size = Math.max(MIN_CHUNK_SIZE, roundedFromCenter);
    maxHeight = map.getFloat("maxVertexHeight");
    minHeight = map.getFloat("minVertexHeight");
    String id = map.get("id");
    Vector2f offset = map.getVector2f("offset");
    int octaves = map.getInt("octaves");
    int seed = map.getInt("seed");
    float persistence = map.getFloat("persistence");
    float scale = map.getFloat("scale");
    float lacunarity = map.getFloat("lacunarity");
    boolean localNormalization = map.getBool("localNormalization");
    data = new ProceduralNoiseData(
      offset,
      persistence,
      lacunarity,
      scale,
      octaves,
      seed,
      localNormalization
    );
    String diffuseTexturePath = map.get("diffuseTexturePath");
    String normalTexturePath = map.get("normalTexturePath");
    String heightMapTexturePath = map.get("heightMapTexturePath");
    material = new Material(id + "_mat");
    material.texture(EMaterialTexture.DIF.value(), diffuseTexturePath);
    material.texture(EMaterialTexture.NRM.value(), normalTexturePath);
    material.texture(EMaterialTexture.HGT.value(), heightMapTexturePath);

    current = generateChunkMesh(new TerrainChunkCoordinate(0, 0), program);
    current.neighbors().forEach(c -> generateChunkMesh(c, program));
  }

  TerrainChunk generateChunkMesh(TerrainChunkCoordinate coordinate, Program program) {
    TerrainChunk chunk = getChunk(coordinate);
    MeshInfo info = chunk.meshInfo();
    if (info.material() == null) info.material(material);
    Mesh mesh = updater.requestOrCreate(-1, () -> info.create(program));
    mesh.bounds().set(chunk.bounds());
    active.putIfAbsent(mesh.glId(), chunk);
    return chunk;
  }

  public void onObserverPositionUpdate(Vector3f position) {
    TerrainChunk chunk = active
      .values()
      .stream()
      .filter(c -> c.contains(position))
      .findFirst()
      .orElse(null);
    TerrainChunkCoordinate chunkCoords = (chunk == null)
                                         ? new TerrainChunkCoordinate(
      Math.round(position.x() / (float) size),
      Math.round(position.z() / (float) size)
    )
                                         : chunk.coordinates;
    if (!chunkCoords.isCoordinate(current.coordinates().x(), current.coordinates.y()))
      recalculateActiveChunks(chunkCoords);
  }

  void recalculateActiveChunks(TerrainChunkCoordinate coordinate) {
    LambdaCounter counter = new LambdaCounter();
    current = activateChunk(coordinate, counter.inc());
    current.neighbors.forEach(c -> activateChunk(c, counter.inc()));
  }

  TerrainChunk activateChunk(TerrainChunkCoordinate coordinate, int i) {
    Integer glId = (Integer) active.keySet().toArray()[i];
    TerrainChunk chunk = getChunk(coordinate);
    Mesh mesh = updater.requestOrCreate(glId, null);
    mesh.redraw(chunk.meshInfo(), v -> mesh.setVertexAttributeArray(program.attributes().point(v)));
    mesh.bounds().set(chunk.bounds);
    active.put(glId, chunk);
    return chunk;
  }

  TerrainChunk getChunk(TerrainChunkCoordinate coordinate) {
    return chunks.computeIfAbsent(coordinate, this::generateTerrainChunk);
  }

  TerrainChunk generateTerrainChunk(TerrainChunkCoordinate coordinate) {
    return new TerrainChunk(coordinate, data, maxHeight, minHeight, size);
  }
}
