package com.game.utils.application;

public class Timeout {
  Thread timeout;
  double duration;
  private Runnable callback;

  public boolean isBusy() {
    return timeout != null && timeout.isAlive();
  }

  public void bounce(double duration, Runnable callback) {
    cancel();
    begin(duration, callback);
  }

  public void begin(double duration, Runnable callback) {
    if (isBusy()) return;
    this.duration = duration;
    this.callback = callback;
    timeout = new Thread(this::onTimeout);
    timeout.start();
  }

  public void cancel() {
    if (isBusy()) timeout.interrupt();
  }

  void onTimeout() {
    long durationNano = Math.round(duration * 1000000000);
    long start = System.nanoTime();
    long delta = System.nanoTime() - start;
    while (delta < durationNano) delta = System.nanoTime() - start;
    callback.run();
  }
}
