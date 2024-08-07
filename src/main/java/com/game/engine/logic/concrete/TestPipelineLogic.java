package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;

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
  }

  @Override
  public void input() {
    captureCameraMovementInput();
    if (scene.window().mouse().isLeftButtonPressed()) scene.rayCastMouseClick(false, hit -> {
      Entity entity = hit.entity();
      String hitEntityText = entity != null ? entity.name() : NO_ENTITY_SELECTED_TEXT;
      // TODO: Make sure this isn't redrawing multiple times per frame...
//      scene.gameText("hit").redraw(hitEntityText);
    });
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
  }
}
