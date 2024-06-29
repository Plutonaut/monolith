package com.game.engine.logic;

import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;
import com.game.engine.window.Window;
import com.game.utils.enums.ERenderer;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Slf4j
public class SafeModeLogic extends AbstractLogic {
  Vector3f viewMovement;
  Vector2f viewRotation;

  Entity skyBox;
  Entity blueShape;

  public SafeModeLogic(GameEngineSettings settings) {
    super(settings);

    viewMovement = new Vector3f();
    viewRotation = new Vector2f();
    log.info("Safe Mode Logic");
  }

  @Override
  public void onStart() {
    renderer.blend(true);
    renderer.depth(true);
    renderer.init(
      ERenderer.SCENE,
      ERenderer.MESH,
      ERenderer.SKYBOX,
      ERenderer.SPRITE,
      ERenderer.BASIC
    );
    scene.lighting()
         .addAmbientLight()
         .addDirectionalLight()
         .addPointLight("test_A")
         .addSpotLight("test_A")
         .addPointLight("test_B")
         .addSpotLight("test_B");
    scene.lighting().directionalLight().factor(0.5f);
    scene.lighting().pointLight("test_A").move(0.75f);
    scene.lighting().spotLight("test_B").pointConeToward(0.5f);

    // frog this works....
//    scene.entities().loadTestQuad("test");
    scene
      .entities()
      .load2DSprite("sprite", "src/main/resources/textures/plaster_wall/plaster_wall.jpg");
//    skyBox = scene.entities().loadSkyBoxModel("skybox", EModel.BASIC_SKYBOX.path());
//    blueShape = scene.entities()
//                     .load3DModel("test", EModel.BLUE_SHAPE.path(), EModel.BLUE_SHAPE.animated())
//                     .scale(0.25f)
//                     .move(0f, 1f, -2.5f);
  }

  @Override
  public void input() {
    Window window = scene.window();

    window.keyboard().input();
    viewMovement.set(window.keyboard().movementVec());

    window.mouse().input();
    viewRotation.set(0f);
    if (window.mouse().isRightButtonPressed()) viewRotation.set(window.mouse().displVec());
//    if (window.mouse().isLeftButtonPressed()) log.info("Left mouse button pressed"); // Raycast pick object
  }

  @Override
  public void update() {
    Camera camera = scene.camera();
    camera.move(viewMovement.x, viewMovement.y, viewMovement.z);
    camera.rotate(viewRotation.x, viewRotation.y, 0f);
  }

  @Override
  public void render() {
    renderer.render(scene);
  }
}
