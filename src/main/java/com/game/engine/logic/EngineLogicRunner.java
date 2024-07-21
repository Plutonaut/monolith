package com.game.engine.logic;

import com.game.engine.settings.EngineSettings;

public class EngineLogicRunner {
  private ILogic current;

  public void use(EngineSettings settings) {
    dispose();

    current = LogicFactory.create(settings.logic().value(), settings);
    current.onStart();
  }

  public ILogic current() {
    return current;
  }

  public boolean running() {
    return current != null && current.isRunning();
  }

  public void dispose() {
    if (current != null) {
      current.onEnd();
      current = null;
    }
  }
}
