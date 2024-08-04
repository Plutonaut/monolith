package com.game.engine;

import org.lwjgl.glfw.GLFW;

public class EngineTimer {
  double maxStepSize;
  double time;
  float elapsed;

  int frames;
  int updates;

  float interval;
  float accumulator;
  double step;

  int fps;
  int ups;

  public EngineTimer(int upsTarget, double maxStepSize) {
    this.maxStepSize = maxStepSize;
    time = getTime();
    elapsed = 0f;
    accumulator = 0f;

    interval = 1f / upsTarget;
    step = 0.0;
  }

  public float currentInterval() {
    return (float) updates * interval;
  }

  public float deltaTime() {
    double now = getTime();
    double delta = Math.min(maxStepSize, now - time);

    time = now;
    elapsed += (float) delta;
    accumulator += (float) delta;

    return (float) delta;
  }

  public boolean isUpdateReady() {
    return accumulator >= interval;
  }

  public void onUpdate() {
    updates++;
    step += interval;
    accumulator -= interval;
  }

  public void onFrameRendered() {
    boolean secondPassed = hasOneSecondElapsed();
    if (secondPassed) {
      fps = frames;
      frames = 0;

      ups = updates;
      updates = 0;
      elapsed -= 1f;
    } else frames++;
  }

  private double getTime() {
    return GLFW.glfwGetTime();
  }

  private boolean hasOneSecondElapsed() {
    return elapsed >= 1f;
  }
}
