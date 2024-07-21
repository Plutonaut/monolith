package com.game.engine.logic;

import com.game.engine.scene.camera.Camera;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.graphics.texture.TextureMapData;
import com.game.utils.enums.EModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TestPipelineLogic extends AbstractLogic {
  Vector3f viewMovement;
  Vector2f viewRotation;

  public TestPipelineLogic(EngineSettings settings) {
    super(settings);
    viewMovement = new Vector3f();
    viewRotation = new Vector2f();
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
//    renderer.cull(true);
    renderer.blend(true);
    renderer.depth(true);
    loadLights();
    scene.generateProceduralTerrain("proc_terrain", proceduralTerrainTextures());
    scene.loadSkyBox3D(EModel.BASIC_SKYBOX).loadText("test", "Sample Text!");
    renderer.bind(scene);
    scene.entity(EModel.BASIC_SKYBOX.name()).scale(50f);
  }

  @Override
  public void input() {
    Window window = scene.window();

    window.keyboard().input();
    viewMovement.set(window.keyboard().movementVec());

    window.mouse().input();
    if (window.mouse().isRightButtonPressed()) viewRotation.set(window.mouse().displVec());
    else viewRotation.set(0f);
  }

  @Override
  public void update() {
    Camera camera = scene.camera();
    camera.move(viewMovement.x, viewMovement.y, viewMovement.z);
    camera.rotate(viewRotation.x, viewRotation.y, 0f);
  }
}
