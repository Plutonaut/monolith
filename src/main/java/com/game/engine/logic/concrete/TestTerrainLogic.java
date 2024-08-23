package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.application.values.ValueMap;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.*;

public class TestTerrainLogic extends AbstractLogic {
  Entity terrainEntity;
//  EntityTerrainController terrainEntityTerrainController;

  Entity treeEntity;

  public TestTerrainLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  protected String windowTitle() {
    return "Test Terrain Generation";
  }

  @Override
  public void onStart() {
    loadSceneLights();
    hud.onStart();

    Entity skyBox = scene
      .createSkyBox(EModel.BASIC_SKYBOX.name(), EModel.BASIC_SKYBOX.path())
      .scale(100f);
//    INIFileModel terrainSettings = INIFileModel.load(TerrainChunkUtils.PROC_TERRAIN_PATH);
    String noiseTerrainId = "noiseTerrain_A";
//    ValueMap noiseTerrainMap = terrainSettings.modelSection(noiseTerrainId);
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
//    terrainEntityTerrainController = terrainEntity.controllers().terrain();
//    terrainEntityTerrainController.generate(noiseTerrainMap);
    ValueMap treeModelValueMap = scene
      .models()
      .builder()
      .id(EModel.TREE_A.name())
      .path(EModel.TREE_A.path())
      .animated(false)
      .instances(9)
      .build();
    treeEntity = scene.createEntity(
      "treeA",
      treeModelValueMap,
      ERenderer.SCENE,
      EProjection.PERSPECTIVE,
      EGLParam.BLEND,
      EGLParam.DEPTH,
      EGLParam.CULL
    ).addPhysics().scale(0.25f);

    ValueStore store = new ValueStore();
//    Matrix4f mat = new Matrix4f();
    Mesh treeMesh = treeEntity.mesh();
//    terrainEntityTerrainController.active().values().forEach(chunk -> store.add(mat
//                                                                                  .identity()
//                                                                                  .scale(4f)
//                                                                                  .translate(
//                                                                                    chunk
//                                                                                      .bounds()
//                                                                                      .origin()
//                                                                                      .x(), treeMesh.bounds().size().y() / 4.1f,
//                                                                                    chunk
//                                                                                      .bounds()
//                                                                                      .origin()
//                                                                                      .z()
//                                                                                  )));
    treeMesh.redraw(EAttribute.IMX, store);

    scene.bind(skyBox, terrainEntity, treeEntity);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
    hud.onInput();
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
//    terrainEntityTerrainController.onObserverPositionUpdate(scene.camera().position());
  }
}
