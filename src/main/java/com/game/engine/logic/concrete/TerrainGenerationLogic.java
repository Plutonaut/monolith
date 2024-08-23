package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.physics.Hit;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.engine.terrain.procedural.TerrainChunk;
import com.game.utils.enums.EModel;
import com.game.utils.logging.PrettifyUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class TerrainGenerationLogic extends AbstractLogic {
  Entity terrainEntity;
  boolean isCenterTextVisible = false;

  public TerrainGenerationLogic(EngineSettings settings) {
    super(settings);
  }

  void toggleHUDCenterText(boolean toggle) {
    if (isCenterTextVisible == toggle) return;

    isCenterTextVisible = toggle;
    if (isCenterTextVisible) hud.showCenterText();
    else hud.hideCenterText();
  }

  void onHit(Hit hit) {
    Entity entity = hit.entity();
    if (entity != null && Objects.equals(entity.name(), terrainEntity.name())) {
      Mesh mesh = entity.meshByGlID(hit.meshId());
      String meshName = mesh.name();
      TerrainChunk chunk = scene.terrain().getTerrainChunkFromName(meshName);
      String s = chunk == null
                 ? "Error: %s".formatted(meshName)
                 : "(X: %d, Y: %d) LOD: %d\nChunk position%s".formatted(
                   chunk.coordinates().x(),
                   chunk.coordinates().y(),
                   chunk.lod(),
                   PrettifyUtils.prettify(chunk.bounds().origin())
                 );
      hud.updateCenterText(s);
      toggleHUDCenterText(true);
    } else toggleHUDCenterText(false);
  }

  @Override
  protected String windowTitle() {
    return "Simplex Noise Procedural Terrain Generation";
  }

  @Override
  public void preRender() {
    scene.updateTerrainOnCameraMovement();
  }

  @Override
  public void onStart() {
    loadSceneLights();
    hud.onStart();
    scene.raycaster().addListener(this::onHit);

    Entity skyBoxEntity = scene
      .createSkyBox(EModel.BASIC_SKYBOX.name(), EModel.BASIC_SKYBOX.path())
      .scale(100);
    terrainEntity = scene.generateTerrain("moss_terrain", "noiseTerrain_A").addPhysics();
    scene.bind(terrainEntity, skyBoxEntity);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
    hud.onInput();
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
  }
}
