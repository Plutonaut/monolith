package com.game.engine;

import com.game.engine.logic.EngineLogicRunner;
import com.game.engine.settings.EngineSettings;

public class GameEngine implements Runnable {
  private final EngineTimer timer;
  private final EngineSettings settings;
  private final EngineLogicRunner logic;

  public GameEngine() {
    settings = new EngineSettings();
    timer = new EngineTimer(settings.targetUPS(), settings.stepSize());
    logic = new EngineLogicRunner();
  }

  public void init() {
    if (settings.threaded()) {
      Thread loop = new Thread(this);
      loop.start();
    }
    else
      run();
  }

  @Override
  public void run() {
    logic.use(settings);
    while (logic.running()) onLoop();
    logic.dispose();
  }

  private void onLoop() {
    float delta = timer.deltaTime();

    logic.current().input();

    while (timer.isUpdateReady()) {
      logic.current().update();
      timer.onUpdate();
    }

    logic.current().render(timer.fps);
    timer.onFrameRendered();
  }
}
