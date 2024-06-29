package com.game.engine;

import com.game.engine.logic.ILogic;
import com.game.engine.logic.LogicFactory;
import com.game.utils.enums.ELogic;

public class MainEngine implements Runnable {
  private final EngineTimer timer;
  private ILogic logic;

  public MainEngine() {
    timer = new EngineTimer(60, 0.25);
  }

  public void init(boolean threaded) {
    if (threaded) {
      Thread loop = new Thread(this);
      loop.start();
    }
    else
      run();
  }

  @Override
  public void run() {
    logic = LogicFactory.create(ELogic.TEST_RENDER.value());
//    logic = LogicFactory.create(ELogic.SAFE_MODE.value());
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
