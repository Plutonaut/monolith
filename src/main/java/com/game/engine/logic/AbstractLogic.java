package com.game.engine.logic;

import com.game.engine.scene.camera.Camera;
import com.game.engine.settings.EngineSettings;
import com.game.engine.render.RenderManager;
import com.game.engine.scene.Scene;
import com.game.engine.window.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class AbstractLogic implements ILogic {
  protected Scene scene;
  protected RenderManager renderer;
  protected Vector3f viewMovement;
  protected Vector2f viewRotation;
  protected int currentFPS;


  public AbstractLogic(EngineSettings settings) {
    scene = new Scene(settings, windowTitle());
    renderer = new RenderManager();
    viewMovement = new Vector3f();
    viewRotation = new Vector2f();
  }

  protected abstract String windowTitle();

  protected void captureCameraMovementInput() {
    Window window = scene.window();

    window.keyboard().input();
    viewMovement.set(window.keyboard().movementVec());

    window.mouse().input();
    if (window.mouse().isRightButtonPressed()) viewRotation.set(window.mouse().displVec());
    else viewRotation.set(0f);
  }

  protected void moveCameraOnUpdate() {
    Camera camera = scene.camera();
    camera.move(viewMovement.x, viewMovement.y, viewMovement.z);
    camera.rotate(viewRotation.x, viewRotation.y, 0f);
  }

  @Override
  public boolean isRunning() {
    return !scene.window().windowShouldClose();
  }

  @Override
  public void render(int fps) {
    currentFPS = fps;
//    scene.window().title("FPS: %s".formatted(fps));
    renderer.render(scene);
  }

  @Override
  public void onEnd() {
    scene.dispose();
    renderer.dispose();
  }
}
