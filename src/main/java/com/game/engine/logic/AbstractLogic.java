package com.game.engine.logic;

import com.game.engine.EngineSettings;
import com.game.engine.render.RenderManager;
import com.game.engine.scene.Scene;

public abstract class AbstractLogic implements ILogic {
  protected Scene scene;
  protected RenderManager renderer;

  public AbstractLogic(EngineSettings settings) {
    scene = new Scene(settings, windowTitle());
    renderer = new RenderManager();
  }

  protected abstract String windowTitle();

  @Override
  public boolean isRunning() {
    return !scene.window().windowShouldClose();
  }

  @Override
  public void render() {
    renderer.render(scene);
  }

  @Override
  public void onEnd() {
    scene.dispose();
    renderer.dispose();
  }
}
