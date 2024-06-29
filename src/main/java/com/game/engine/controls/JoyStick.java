package com.game.engine.controls;

import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWJoystickCallback;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class JoyStick {
  private final List<Integer> joysticks;
  private GLFWJoystickCallback callback;

  public JoyStick() {
    joysticks = new ArrayList<>();
  }

  public void setupCallback() {
    callback = GLFW.glfwSetJoystickCallback(this::onChange);
  }

  void onChange(int id, int event) {
    if (event == GLFW.GLFW_CONNECTED) joysticks.add(id);
    else if (event == GLFW.GLFW_DISCONNECTED) joysticks.remove(id);
  }

  public void dispose() {
    callback = GLFW.glfwSetJoystickCallback(null);
    if (callback != null) callback.free();
  }
}
