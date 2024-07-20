package com.game.engine.logic;

import com.game.engine.settings.EngineSettings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogicFactory {
//  public static ILogic create(EngineSettings settings) {
  public static ILogic create(String logic, EngineSettings settings) {
    return switch (logic) {
      case "testRender" -> new TestRenderLogic(settings);
      case "testPipeline" -> new TestPipelineLogic(settings);
      default -> new SafeModeLogic(settings);
    };
  }
}
