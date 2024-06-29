package com.game.engine.controls;

import com.game.utils.enums.EControls;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Keyboard {
  private final HashMap<EControls, Integer> controls;
  private final List<Integer> active;
  private final Vector3f movementVec;

  public Keyboard() {
    controls = new EControlsIntegerHashMap();
    active = new ArrayList<>();
    movementVec = new Vector3f();
  }

  public void onKeyPress(long window, int key, int scancode, int action, int mods) {
    if (action == GLFW.GLFW_PRESS && !isKeyPressed(key)) {
      active.add(key);
    } else if (action == GLFW.GLFW_RELEASE && isKeyPressed(key)) {
      if (key == GLFW.GLFW_KEY_ESCAPE) GLFW.glfwSetWindowShouldClose(window, true);
      else active.remove(active.indexOf(key));
    }
  }

  public boolean isKeyPressed(EControls control) {
    return isKeyPressed(controls.get(control));
  }

  public boolean isKeyPressed(int keyCode) {
    return active.contains(keyCode);
  }

  public boolean isKeyPressed(long handle, int keyCode) {
    return GLFW.glfwGetKey(handle, keyCode) == GLFW.GLFW_PRESS;
  }

  public Keyboard changeControls(EControls control, int keyCode) {
    controls.put(control, keyCode);

    return this;
  }

  public void input() {
    reset();

    controls.forEach((k, v) -> {
      if (active.contains(v)) {
        switch (k) {
          case MOVE_FORWARD -> movementVec.z = -1;
          case MOVE_BACKWARD -> movementVec.z = 1;
          case MOVE_LEFT -> movementVec.x = -1;
          case MOVE_RIGHT -> movementVec.x = 1;
          case MOVE_UP -> movementVec.y = -1;
          case MOVE_DOWN -> movementVec.y = 1;
        }
      }
    });
  }

  public void reset() {
    movementVec.set(0f);
  }

  private static class EControlsIntegerHashMap extends HashMap<EControls, Integer> {
    {
      put(EControls.MOVE_UP, GLFW.GLFW_KEY_E);
      put(EControls.MOVE_DOWN, GLFW.GLFW_KEY_Q);
      put(EControls.MOVE_FORWARD, GLFW.GLFW_KEY_W);
      put(EControls.MOVE_BACKWARD, GLFW.GLFW_KEY_S);
      put(EControls.MOVE_LEFT, GLFW.GLFW_KEY_A);
      put(EControls.MOVE_RIGHT, GLFW.GLFW_KEY_D);
    }
  }
}
