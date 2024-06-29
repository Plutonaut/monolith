package com.game.engine.logic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogicFactory {
  public static ILogic create(String type) {
    GameEngineSettings settings = new GameEngineSettings();
    settings.windowTitle("Safe Mode Game Engine");

    return switch (type) {
      case "testRender" -> new TestRenderLogic(settings);
      default -> new SafeModeLogic(settings);
    };
  }
}
