package com.game.engine.controls;

import java.util.HashMap;
import java.util.function.Consumer;

public class InputDevice {
  protected final HashMap<Integer, Consumer<Integer>> listeners;

  public InputDevice() {
    listeners = new HashMap<>();
  }

  public void addListener(int input, Consumer<Integer> listener) {
    listeners.put(input, listener);
  }

  public void removeListener(int key) { listeners.remove(key); }

  protected void emit(int input, int action) {
    if (listeners.containsKey(input)) listeners.get(input).accept(action);
  }
}
