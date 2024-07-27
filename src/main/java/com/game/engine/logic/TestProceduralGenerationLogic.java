package com.game.engine.logic;

import com.game.engine.settings.EngineSettings;
import com.game.utils.engine.entity.StaticEntityData;
import com.game.utils.enums.EModel;

public class TestProceduralGenerationLogic extends AbstractLogic {
  public TestProceduralGenerationLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  protected String windowTitle() {
    return "Test Procedural Generation";
  }

  @Override
  public void onStart() {
    scene
      .cull(true)
      .blend(true)
      .depth(true)
      .loadSkyBox3D(EModel.BASIC_SKYBOX)
      .generateProceduralTerrain("procedural_moss", StaticEntityData.MOSS_TEXTURE_MAP_DATA)
      .loadText("hud", "HUD Display:");
    renderer.bind(scene);
    scene.entity(EModel.BASIC_SKYBOX.name()).scale(50f);
    scene.entity("procedural_moss").addPhysics().scale(10);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
  }

  @Override
  public void update() {
    moveCameraOnUpdate();
  }
}
