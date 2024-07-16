package com.game.engine;

import com.game.engine.logic.ILogic;
import com.game.engine.logic.LogicFactory;
import com.game.engine.settings.EngineSettings;

public class MainEngine implements Runnable {
  private final EngineTimer timer;
  private final EngineSettings settings;
  private ILogic logic;

  public MainEngine() {
    settings = new EngineSettings();
    timer = new EngineTimer(settings.targetUPS(), settings.stepSize());
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
    logic = LogicFactory.create(settings);
    logic.onStart();

    while (logic.isRunning()) onLoop();

    logic.onEnd();
  }

  private void onLoop() {
    float delta = timer.deltaTime();

    logic.input();

    while (timer.isUpdateReady()) {
      logic.update();
      timer.onUpdate();
    }

    logic.render();
    timer.onFrameRendered();
  }
}
