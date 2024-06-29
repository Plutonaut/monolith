package com.game.engine.controls;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

@Accessors(fluent = true)
@Data
public class Mouse {
  private final Vector2f position;
  private final Vector2f previous;
  private final Vector2f displVec;

  private boolean inWindow;

  private boolean leftPressed;
  private boolean rightPressed;

  public Mouse() {
    position = new Vector2f();
    previous = new Vector2f(-1, -1);
    displVec = new Vector2f();

    leftPressed = false;
    rightPressed = false;
    inWindow = false;
  }

  public void onPositionChanged(long handle, double x, double y) {
    this.position.x = (float) x;
    this.position.y = (float) y;
  }

  public void onClick(long handle, int button, int pressed, int m) {
    boolean isPressed = pressed == GLFW.GLFW_PRESS;
    leftPressed(button == GLFW.GLFW_MOUSE_BUTTON_1 && isPressed);
    rightPressed(button == GLFW.GLFW_MOUSE_BUTTON_2 && isPressed);
  }

  public void onCursorEnter(long handle, boolean entered) {
    inWindow = entered;
  }

  // Want to figure this out on my own...
  public void input() {
    displVec.x = 0;
    displVec.y = 0;
    if (previous.x > 0 && previous.y > 0 && inWindow) {
      final double deltax = position.x - previous.x;
      final double deltay = position.y - previous.y;
      final boolean rotateX = deltax != 0;
      final boolean rotateY = deltay != 0;
      if (rotateX)
        displVec.y = (float) deltax;
      if (rotateY)
        displVec.x = (float) deltay;
    }
    previous.x = position.x;
    previous.y = position.y;
  }

  public boolean isLeftButtonPressed() {
    return leftPressed;
  }

  public boolean isRightButtonPressed() {
    return rightPressed;
  }
}
