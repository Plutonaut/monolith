package com.game.engine.logic;

import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.engine.entity.StaticEntityData;
import com.game.utils.enums.EModel;
import com.game.utils.enums.ERenderer;

public class TestPipelineLogic extends AbstractLogic {
  static final String FPS_HUD_TEXT = "FPS: %d";
  static final String NO_ENTITY_SELECTED_TEXT = "<Left click to select an entity>";

  public TestPipelineLogic(EngineSettings settings) {
    super(settings);
  }

  void loadLights() {
    scene
      .lighting()
      .addAmbientLight()
      .addDirectionalLight()
      .addPointLight("test_A")
      .addSpotLight("test_A")
      .addPointLight("test_B")
      .addSpotLight("test_B");
    scene.lighting().directionalLight().factor(0.5f);
    scene.lighting().pointLight("test_A").move(0.75f);
    scene.lighting().spotLight("test_B").pointConeToward(0.5f);
  }

  @Override
  public String windowTitle() {
    return "TEST Render Pipeline";
  }

  @Override
  public void onStart() {
    loadLights();
    scene
      .cull(true)
      .blend(true)
      .depth(true)
      .loadSkyBox3D(EModel.BASIC_SKYBOX)
      .loadText("hud", FPS_HUD_TEXT.formatted(0))
      .loadText("hit", NO_ENTITY_SELECTED_TEXT)
      .load3D(ERenderer.SCENE, EModel.CUBE)
      .load3D(ERenderer.SCENE, EModel.CUBE)
      .load3D(ERenderer.SCENE, EModel.RAILWAY_PART)
      .generateProceduralTerrain("proc_terrain", StaticEntityData.MOSS_TEXTURE_MAP_DATA);
    renderer.bind(scene);
    scene.entity(EModel.BASIC_SKYBOX.name()).scale(50f);
    scene.entity("proc_terrain").scale(5).addPhysics();
    scene.entity(EModel.CUBE.name()).move(0f, 0.5f, 0f).scale(0.5f).addPhysics();
    scene.entity(EModel.RAILWAY_PART.name()).move(0f, -0.5f, 0f).scale(0.25f).addPhysics();
    scene.gameText("hud").move(20, 25);
    scene.gameText("hit").move(20, 75);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
    if (scene.window().mouse().isLeftButtonPressed()) scene.rayCastMouseClickClosest(hit -> {
      Entity entity = hit.entity();
      String hitEntityText = entity != null ? entity.name() : NO_ENTITY_SELECTED_TEXT;
      // TODO: Make sure this isn't redrawing multiple times per frame...
      scene.gameText("hit").redraw(hitEntityText);
    });
  }

  @Override
  public void update() {
    moveCameraOnUpdate();
    // TODO: Move to method that can only be called once per frame.
    scene.gameText("hud").redraw(FPS_HUD_TEXT.formatted(currentFPS));
  }
}
