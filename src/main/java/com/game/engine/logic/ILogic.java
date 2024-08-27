package com.game.engine.logic;

public interface ILogic {
  boolean isRunning();
  void onStart();
  void input();
  void preUpdate(float delta);
  void update(float interval);
  void preRender(float alpha);
  void render(int fps);
  void onEnd();
}
