package com.game.engine.logic;

import com.game.engine.scene.camera.Camera;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.utils.enums.ERenderer;
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
//    scene.load3D(ERenderer.SCENE, EModel.CUBE);
//    scene.load3D(ERenderer.MESH, EModel.CUBE);
//    scene.load3D(ERenderer.SCENE, EModel.RAILWAY_PART);
    scene.generateProceduralTerrain(
      ERenderer.SCENE,
      "proc_terrain",
      128,
      128,
      "src/main/resources/textures/moss_surface/moss_surface_albedo.png",
      "src/main/resources/textures/moss_surface/moss_surface_height.png",
      1
    );
//    scene.loadSkyBox3D(EModel.BASIC_SKYBOX).loadText("test", "Sample Text!");
//    scene.loadSkyBox3D(EModel.BASIC_SKYBOX).load3D(EModel.CUBE);
//    scene.load3D(EModel.CUBE).load3D(EModel.CUBE).load2D(ESprite.IGGY).loadText("test_text", "TEST");

    renderer.bind(scene);
//
//    scene.entity(EModel.CUBE.name()).move(0f, 0.5f, 0f).scale(0.5f);
//    scene
//      .entity(ESprite.IGGY.atlasName())
//      .move(scene.window().width() / 2f, scene.window().height() / 2f)
//      .scale(32);
//
//    scene.entity(EModel.BASIC_SKYBOX.name()).scale(50f);
//    scene.entity("proc_terrain").move(0f, 0f, 0f).scale(20f);
//    scene.gameText("test").move(25, 100);
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
