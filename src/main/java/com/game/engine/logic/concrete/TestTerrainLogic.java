package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.loaders.ini.INIFileModel;
import com.game.utils.application.values.ValueMap;
import com.game.utils.engine.terrain.procedural.EntityTerrainController;
import com.game.utils.enums.EGLParam;
import com.game.utils.enums.EModel;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;

public class TestTerrainLogic extends AbstractLogic {
  Entity terrainEntity;
  EntityTerrainController terrainEntityTerrainController;

//  Entity treeEntity;

  public TestTerrainLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  protected String windowTitle() {
    return "Test Terrain Generation";
  }

  @Override
  public void onStart() {
    loadLights();
    hud.onStart();

    Entity skyBox = scene
      .createSkyBox(EModel.BASIC_SKYBOX.name(), EModel.BASIC_SKYBOX.path())
      .scale(100f);
    INIFileModel terrainSettings = INIFileModel.load("properties/ini/proc_terrain_1.ini");
    String noiseTerrainId = "noiseTerrain_A";
    ValueMap noiseTerrainMap = terrainSettings.modelSection(noiseTerrainId);
    Model model = new Model(noiseTerrainId);
    terrainEntity = scene.createEntity(
      noiseTerrainId,
      model,
      ERenderer.TERRAIN,
      EProjection.PERSPECTIVE,
      EGLParam.CULL,
      EGLParam.DEPTH,
      EGLParam.BLEND
    ).addPhysics();
//    ).scale(noiseTerrainMap.getFloat("size")).addPhysics();
    terrainEntityTerrainController = terrainEntity.controllers().terrain();
    terrainEntityTerrainController.generate(noiseTerrainMap);
//    ValueMap treeModelValueMap = scene
//      .models()
//      .builder()
//      .id(EModel.TREE_A.name())
//      .path(EModel.TREE_A.path())
//      .animated(false)
//      .instances(9).build();
//    treeEntity = scene.createEntity(
//      "treeA",
//      treeModelValueMap,
//      ERenderer.SCENE,
//      EProjection.PERSPECTIVE,
//      EGLParam.BLEND,
//      EGLParam.DEPTH,
//      EGLParam.CULL
//    ).addPhysics().scale(0.25f);

//    ValueStore store = new ValueStore();
//    terrainEntityTerrainController
//      .getChunkStream()
//      .forEach(chunk -> store.add(chunk.modelMatrix()));
//    treeEntity.mesh().redraw(EAttribute.IMX, store);

    scene.bind(skyBox, terrainEntity);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
    hud.onInput();
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
    terrainEntityTerrainController.onObserverPositionUpdate(scene.camera().position());
  }
}
