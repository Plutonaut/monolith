package com.game.engine.logic;

import com.game.engine.settings.EngineSettings;
import com.game.graphics.texture.TextureMapData;
import com.game.utils.enums.EModel;
import com.game.utils.enums.ERenderer;

public class TestPipelineLogic extends AbstractLogic {
  static final String FPS_HUD_TEXT = "HUD Display Text: %d";

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

  TextureMapData proceduralTerrainTextures() {
    String directory = "src/main/resources/textures/moss_surface/";
    return new TextureMapData()
      .diffuse(directory + "moss_surface_albedo.png")
      .normal(directory + "moss_surface_normal.png")
      .height(directory + "moss_surface_height.png");
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
      .load3D(ERenderer.SCENE, EModel.CUBE)
      .load3D(ERenderer.SCENE, EModel.CUBE)
      .load3D(ERenderer.SCENE, EModel.RAILWAY_PART)
      .generateProceduralTerrain("proc_terrain", proceduralTerrainTextures());
    renderer.bind(scene);
    scene.entity(EModel.BASIC_SKYBOX.name()).scale(50f);
    scene.entity("proc_terrain").scale(5);
    scene.entity(EModel.CUBE.name()).move(0f, 0.5f, 0f).scale(0.5f);
    scene.entity(EModel.RAILWAY_PART.name()).move(0f, -0.5f, 0f).scale(0.25f);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
  }

  @Override
  public void update() {
    moveCameraOnUpdate();
    scene.gameText("hud").redraw(FPS_HUD_TEXT.formatted(currentFPS));
//    TextEntity textEntity = scene.gameText("hud");
//    String updatedFPSText = FPS_HUD_TEXT.formatted(currentFPS);
//    if (!textEntity.text().equals(updatedFPSText))
//      scene.redrawText("hud", updatedFPSText);
  }
}
