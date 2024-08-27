package com.game.engine.scene.terrain;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.loaders.ini.INIFileModel;
import com.game.utils.application.RandomNumberGenerator;
import com.game.utils.application.values.ValueGrid;
import com.game.utils.application.values.ValueMap;
import com.game.utils.application.values.ValueStore;
import com.game.utils.engine.terrain.procedural.ProceduralNoiseUtils;
import com.game.utils.engine.terrain.procedural.TerrainChunkUtils;
import com.game.utils.enums.EMaterialTexture;
import com.game.utils.enums.ERenderer;
import com.game.utils.logging.PrettifyUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;

@Slf4j
@Accessors(fluent = true)
public class TerrainChunkManager {
  static final int SIZE = 32;
  static final int POINTS = 8;

  protected final HashMap<TerrainChunkCoordinate, TerrainChunk> chunks;
  @Getter
  protected final HashSet<TerrainChunk> active;
  @Getter
  protected final ValueStore spawnPoints;
  protected final Program terrainShader;
  protected final Vector3f lastPosition;
  protected TerrainChunk current;

  @Getter
  protected int maxLOD;
  @Getter
  protected int minLOD;
  @Getter
  protected int numChunksVisible;

  public TerrainChunkManager() {
    terrainShader = GlobalCache.instance().program(ERenderer.TERRAIN.key());
    lastPosition = new Vector3f();
    spawnPoints = new ValueStore();
    chunks = new HashMap<>();
    active = new HashSet<>();
    maxLOD = 0;
    minLOD = 4;
  }

  public TerrainChunk[] getSortedChunkArray() {
    return active.stream().sorted(TerrainChunkUtils::compareChunks).toArray(TerrainChunk[]::new);
  }

  public Model loadFromFile(String terrainName) {
    return loadFromFile(terrainName, TerrainChunkUtils.PROC_TERRAIN_PATH);
  }

  public Model loadFromFile(String terrainName, String fileName) {
    INIFileModel terrainSettings = INIFileModel.load(fileName);
    ValueMap terrainMap = terrainSettings.modelSection(terrainName);
    return generate(terrainMap);
  }

  public Model generate(ValueMap map) {
    Vector2f visibleLOD = map.getVector2f("visibleLOD");
    maxLOD = Math.round(Math.min(visibleLOD.x, visibleLOD.y));
    minLOD = Math.round(Math.max(visibleLOD.x, visibleLOD.y));
    int maxViewDistance = map.getInt("maxViewDistance");
    numChunksVisible = Math.ceilDiv(maxViewDistance, SIZE);
    int numChunksVisibleFromEdge = numChunksVisible * 2;
    int totalNumValues = numChunksVisibleFromEdge * SIZE;
    ProceduralNoiseData data = ProceduralNoiseData.createFromMap(map);
    Vector3f terrainOrigin = new Vector3f();
    if (map.has("terrainOrigin")) terrainOrigin.set(map.getVector3f("terrainOrigin"));
    TerrainChunkCoordinate startingCoordinate = TerrainChunkUtils.convertPositionToCoordinates(
      terrainOrigin,
      SIZE
    );

    ValueGrid terrainGrid = ProceduralNoiseUtils.process(data, totalNumValues, totalNumValues);
    for (int x = 0; x < numChunksVisibleFromEdge; x++) {
      for (int y = 0; y < numChunksVisibleFromEdge; y++) {
        int chunkXCoordinate = x - numChunksVisible + startingCoordinate.x();
        int chunkYCoordinate = y - numChunksVisible + startingCoordinate.y();
        TerrainChunkCoordinate coordinate = new TerrainChunkCoordinate(
          chunkXCoordinate,
          chunkYCoordinate
        );

        updateTerrainChunkGrid(coordinate, terrainGrid, x, y);
      }
    }

    onObserverPositionUpdate(terrainOrigin);

    String id = map.get("id");
    Material material = new Material(id + "_mat");
    material.texture(EMaterialTexture.DIF.value(), map.get("diffuseTexturePath"));
    material.texture(EMaterialTexture.NRM.value(), map.get("normalTexturePath"));
    material.texture(EMaterialTexture.HGT.value(), map.get("heightMapTexturePath"));
    Model model = new Model(id);
    spawnPoints.clear();
    RandomNumberGenerator rng = new RandomNumberGenerator(data.seed());
    active.forEach((terrainChunk -> {
      MeshInfo info = terrainChunk.meshInfo();
      info.material(material);
      model.addMeshData(info.name());

      Vector3f min = info.bounds().minVertex();
      for (int i = 0; i < POINTS; i++) {
        float x = rng.nextf(SIZE);
        float z = rng.nextf(SIZE);

        Vector3f position = new Vector3f(x, 0.5f, z).add(min);
        spawnPoints.add(position);
      }
    }));

    return model;
  }

  public TerrainChunk getTerrainChunkFromName(String name) {
    return active()
      .stream()
      .filter(chunk -> chunk.coordinates().toString().equalsIgnoreCase(name))
      .findFirst()
      .orElse(null);
  }

  public void onObserverPositionUpdate(Vector3f position) {
//    if (current == null || lastPosition.distance(position) >= (SIZE / 2f)) recalculateTerrainChunks(position);
    if (current == null || !current.contains(position)) recalculateTerrainChunks(position);
  }

  void recalculateTerrainChunks(Vector3f position) {
    log.debug("Recalculating terrain chunks for position {}", PrettifyUtils.prettify(position));
    chunks.forEach((coordinate, terrainChunk) -> {
      int chunkLODIndex = terrainChunk.calculateLOD(position, maxLOD);
      if (chunkLODIndex >= maxLOD && chunkLODIndex < minLOD) {
        if (terrainChunk.contains(position)) {
          log.debug("Current chunk updated to {}", terrainChunk.coordinates().toString());
          current = terrainChunk;
        }
        active.add(terrainChunk);
        recalculateTerrainChunkMesh(terrainChunk);
      } else active.remove(terrainChunk);
    });
    lastPosition.set(position);
  }

  void recalculateTerrainChunkMesh(TerrainChunk chunk) {
    MeshInfo info = chunk.updateMeshInfo();
    if (info == null) return;

    Mesh mesh = GlobalCache.instance().mesh(chunk.coordinates().toString());
    if (mesh.vboAttributeKeyMap().isEmpty()) mesh.redrawAttributes(info, terrainShader);
    else mesh.redraw(info, (v) -> { });
  }

  void updateTerrainChunkGrid(
    TerrainChunkCoordinate coordinate, ValueGrid grid, int startX, int startY
  ) {
    TerrainChunk chunk = getTerrainChunk(coordinate);
    int gridStartX = startX * SIZE;
    int gridStartY = startY * SIZE;

    if (startX > 0) gridStartX -= 1;
    if (startY > 0) gridStartY -= 1;

    for (int x = 0; x < SIZE + 1; x++) {
      for (int y = 0; y < SIZE + 1; y++) {
        float heightMapValue = grid.get(gridStartX + x, gridStartY + y);
        chunk.grid().set(y, x, heightMapValue);
      }
    }
    chunk.calculateBounds(chunk.grid().min(), chunk.grid().max());
  }

  TerrainChunk getTerrainChunk(TerrainChunkCoordinate coordinate) {
    return chunks.computeIfAbsent(coordinate, (c) -> new TerrainChunk(c, SIZE));
  }
}
