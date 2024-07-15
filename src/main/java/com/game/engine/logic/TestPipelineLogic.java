package com.game.engine.logic;

import com.game.engine.scene.camera.Camera;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.utils.enums.EModel;
import com.game.utils.enums.ESprite;
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
    scene.load3D(EModel.CUBE).load3D(EModel.CUBE).load2D(ESprite.IGGY).loadText("test_text", "TEST");
    renderer.bind(scene);

    scene.entity(EModel.CUBE.name()).move(0f, 1.5f, 0f).scale(0.5f);
    scene
      .entity(ESprite.IGGY.atlasName())
      .move(scene.window().width() / 2f, scene.window().height() / 2f)
      .scale(32);

    scene.gameText("test_text").move(scene.window().width() / 3f, scene.window().height() / 3f).scale(64);
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
