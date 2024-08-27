package com.game.engine.logic;

import com.game.engine.logic.concrete.*;
import com.game.engine.settings.EngineSettings;

public class EngineLogicRunner {
  private ILogic current;

  public void use(EngineSettings settings) {
    dispose();

    current = create(settings);
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

  ILogic create(EngineSettings settings) {
    String logic = settings.logic();
    return switch (logic) {
      case "genTerrain" -> new TerrainGenerationLogic(settings);
      case "testPS" -> new TestParticleSystemLogic(settings);
      case "testTerrain" -> new TestTerrainLogic(settings);
      case "testInstancing" -> new TestInstancingLogic(settings);
      case "testRender" -> new TestRenderLogic(settings);
      case "testProcGen" -> new TestProceduralGenerationLogic(settings);
      case "sandbox3D" -> new Sandbox3DLogic(settings);
      case "sandbox2D" -> new Sandbox2DLogic(settings);
      default -> new SafeModeLogic(settings);
    };
  }
}
