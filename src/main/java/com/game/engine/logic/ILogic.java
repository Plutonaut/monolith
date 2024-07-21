package com.game.engine.logic;

public interface ILogic {
  boolean isRunning();
  void onStart();
  void input();
  void update();
  void render(float fps);
  void onEnd();
}
