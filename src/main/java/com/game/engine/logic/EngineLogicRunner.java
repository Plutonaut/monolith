package com.game.engine.logic;

import com.game.engine.settings.EngineSettings;

public class EngineLogicRunner {
  private ILogic current;

  public void use(EngineSettings settings) {
    dispose();

    current = create(settings.logic().value(), settings);
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

  ILogic create(String logic, EngineSettings settings) {
    return switch (logic) {
      case "testRender" -> new TestRenderLogic(settings);
      case "testPipeline" -> new TestPipelineLogic(settings);
      case "testProcGen" -> new TestProceduralGenerationLogic(settings);
      default -> new SafeModeLogic(settings);
    };
  }
}
