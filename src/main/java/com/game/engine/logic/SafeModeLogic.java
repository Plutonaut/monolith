package com.game.engine.logic;

import com.game.engine.settings.EngineSettings;
import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;
import com.game.engine.window.Window;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Slf4j
public class SafeModeLogic extends AbstractLogic {
  Vector3f viewMovement;
  Vector2f viewRotation;

  Entity skyBox;
  Entity blueShape;

  public SafeModeLogic(EngineSettings settings) {
    super(settings);

    viewMovement = new Vector3f();
    viewRotation = new Vector2f();
    log.info("Safe Mode Logic");
  }

  @Override
  public String windowTitle() {
    return "SAFE MODE";
  }

  @Override
  public void onStart() {
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
  public void update(float interval) {
    Camera camera = scene.camera();
    camera.move(viewMovement.x, viewMovement.y, viewMovement.z);
    camera.rotate(viewRotation.x, viewRotation.y, 0f);
  }
}
