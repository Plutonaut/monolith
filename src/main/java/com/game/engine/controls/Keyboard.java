package com.game.engine.controls;

import com.game.utils.enums.EControls;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.function.Consumer;

@Accessors(fluent = true)
@Data
public class Keyboard {
  private final HashMap<Integer, Consumer<Integer>> listeners;
  private final Vector3f movementVec;

  public Keyboard() {
    listeners = new HashMap<>();
    movementVec = new Vector3f();
  }

  public void onKeyPress(long window, int key, int scancode, int action, int mods) {
    if (key == GLFW.GLFW_KEY_ESCAPE) GLFW.glfwSetWindowShouldClose(window, true);

    else if (listeners().containsKey(key)) emit(key, action);
  }

  public void onKeyPress(int key, Consumer<Integer> callback) {
    listeners.put(key, callback);
  }

  public void captureMovement(EControls control) {
    switch (control) {
      case MOVE_FORWARD -> movementVec.z = -1;
      case MOVE_BACKWARD -> movementVec.z = 1;
      case MOVE_LEFT -> movementVec.x = -1;
      case MOVE_RIGHT -> movementVec.x = 1;
      case MOVE_UP -> movementVec.y = -1;
      case MOVE_DOWN -> movementVec.y = 1;
    }
  }

  public void removeListener(int key) { listeners.remove(key); }

  void emit(int key, int action) {
    listeners.get(key).accept(action);
  }

  public void reset() {
    movementVec.set(0f);
  }
}
