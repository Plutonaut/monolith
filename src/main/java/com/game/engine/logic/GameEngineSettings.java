package com.game.engine.logic;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public class GameEngineSettings {
  private String windowTitle;
  private int windowWidth;
  private int windowHeight;

  public GameEngineSettings() {
    windowTitle = "Game Engine";
    windowWidth = 800;
    windowHeight = 600;
  }
}
