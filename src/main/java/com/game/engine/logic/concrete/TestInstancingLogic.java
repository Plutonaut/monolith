package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.settings.EngineSettings;

public class TestInstancingLogic extends AbstractLogic {
  public TestInstancingLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  protected String windowTitle() {
    return "Instance Render Test";
  }

  @Override
  public void onStart() {

  }

  @Override
  public void input() {
    captureCameraMovementInput();
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
  }
}
