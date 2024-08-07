package com.game.engine.logic;

import com.game.engine.controls.Keyboard;
import com.game.engine.controls.Mouse;
import com.game.engine.render.RenderManager;
import com.game.engine.scene.Scene;
import com.game.engine.scene.camera.Camera;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.utils.enums.EControls;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

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
    Mouse mouse = window.mouse();
    Keyboard keyboard = window.keyboard();

    keyboard.reset();
    if (window.isKeyPressed(GLFW.GLFW_KEY_W)) keyboard.captureMovement(EControls.MOVE_FORWARD);
    if (window.isKeyPressed(GLFW.GLFW_KEY_S)) keyboard.captureMovement(EControls.MOVE_BACKWARD);
    if (window.isKeyPressed(GLFW.GLFW_KEY_A)) keyboard.captureMovement(EControls.MOVE_LEFT);
    if (window.isKeyPressed(GLFW.GLFW_KEY_D)) keyboard.captureMovement(EControls.MOVE_RIGHT);
    if (window.isKeyPressed(GLFW.GLFW_KEY_E)) keyboard.captureMovement(EControls.MOVE_UP);
    if (window.isKeyPressed(GLFW.GLFW_KEY_Q)) keyboard.captureMovement(EControls.MOVE_DOWN);
    viewMovement.set(keyboard.movementVec());

    viewRotation.set(0f);
    mouse.input();
    if (mouse.isRightButtonPressed()) viewRotation.set(mouse.displVec());
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
    scene.window().title("FPS: %s".formatted(fps));
    renderer.render(scene);
  }

  @Override
  public void onEnd() {
    scene.dispose();
    renderer.dispose();
  }
}
