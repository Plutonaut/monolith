package com.game.engine.logic;

import com.game.engine.EngineSettings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogicFactory {
  public static ILogic create(EngineSettings settings) {
    return switch (settings.logic().value()) {
      case "testRender" -> new TestRenderLogic(settings);
      case "testPipeline" -> new TestPipelineLogic(settings);
      default -> new SafeModeLogic(settings);
    };
  }
}
