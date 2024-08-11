package com.game.engine.controls;

import com.game.utils.enums.EControls;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

@Getter
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public class Keyboard extends InputDevice {
  private final Vector3f movementVec;

  public Keyboard() {
    super();

    movementVec = new Vector3f();
  }

  public void onKeyPress(long window, int key, int scancode, int action, int mods) {
    if (key == GLFW.GLFW_KEY_ESCAPE) GLFW.glfwSetWindowShouldClose(window, true);
    else emit(key, action);
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

  public void reset() {
    movementVec.set(0f);
  }
}
