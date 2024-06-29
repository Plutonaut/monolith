package com.game.engine.logic;

import com.game.engine.render.RenderManager;
import com.game.engine.scene.Scene;

public abstract class AbstractLogic implements ILogic {
  protected Scene scene;
  protected RenderManager renderer;

  public AbstractLogic(GameEngineSettings settings) {
    scene = new Scene(settings.windowWidth(), settings.windowHeight(), settings.windowTitle());
    renderer = new RenderManager();
  }

  @Override
  public boolean isRunning() {
    return !scene.window().windowShouldClose();
  }

  @Override
  public void onEnd() {
    scene.dispose();
    renderer.dispose();
  }
}
