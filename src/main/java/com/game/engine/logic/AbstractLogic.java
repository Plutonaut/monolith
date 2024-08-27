package com.game.engine.logic;

import com.game.engine.controls.Keyboard;
import com.game.engine.controls.Mouse;
import com.game.engine.render.RenderManager;
import com.game.engine.scene.hud.Hud;
import com.game.engine.scene.Scene;
import com.game.engine.scene.camera.Camera;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.utils.enums.EControls;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public abstract class AbstractLogic implements ILogic {
  protected final RenderManager renderer;
  protected final Scene scene;
  protected final Hud hud;

  protected Vector3f viewMovement;
  protected Vector2f viewRotation;
  protected int currentFPS;
  protected float currentDelta;

  public AbstractLogic(EngineSettings settings) {
    renderer = new RenderManager();
    scene = new Scene(settings, windowTitle());
    hud = new Hud(scene, settings);

    viewMovement = new Vector3f();
    viewRotation = new Vector2f();
  }

  protected abstract String windowTitle();

  protected void loadSceneLights() {
    scene.lighting().addAmbientLight().addDirectionalLight().addPointLight("test_A").addSpotLight(
      "test_A").addPointLight("test_B").addSpotLight("test_B");
    scene.lighting().directionalLight().factor(0.5f);
    scene.lighting().pointLight("test_A").move(0.75f);
    scene.lighting().spotLight("test_B").pointConeToward(0.5f);
  }

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
  public void preUpdate(float delta) {
    currentDelta = delta;
  }

  @Override
  public void preRender(float delta) {}

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
