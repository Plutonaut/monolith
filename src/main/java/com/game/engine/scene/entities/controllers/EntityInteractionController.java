package com.game.engine.scene.entities.controllers;

import com.game.engine.controls.Mouse;
import com.game.engine.physics.Bounds2D;
import com.game.engine.scene.entities.IEntityInteractionCallback;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.engine.PhysicsUtils;
import com.game.utils.enums.EController;
import com.game.utils.enums.EGUIEvent;

import java.util.HashSet;

public class EntityInteractionController extends AbstractEntityController {
  protected final HashSet<IEntityInteractionCallback> listeners;
  protected boolean hover;
  protected int leftMouse;
  protected int rightMouse;

  public EntityInteractionController() {
    listeners = new HashSet<>();
  }

  @Override
  public String type() {
    return EController.INTERACTION.value();
  }

  @Override
  public void onUpdate(ModelTransform transform) {

  }

  public void listen(IEntityInteractionCallback listener) {
    this.listeners.add(listener);
  }

  public void onMouseInput(Mouse mouse, Bounds2D bounds) {
    boolean isInside = PhysicsUtils.isPointInsideBounds2D(mouse.position(), bounds);

    if (isInside != hover) {
      hover = isInside;
      emit(isInside ? EGUIEvent.ENTER : EGUIEvent.EXIT);

      if (mouse.leftPressed() != leftMouse) {
        leftMouse = mouse.leftPressed();
        emit(mouse.isLeftButtonPressed() ? EGUIEvent.LEFT_CLICK : EGUIEvent.LEFT_RELEASE);
      }
      if (mouse.rightPressed() != rightMouse) {
        rightMouse = mouse.rightPressed();
        emit(mouse.isRightButtonPressed() ? EGUIEvent.RIGHT_CLICK : EGUIEvent.LEFT_RELEASE);
      }
    }
  }

  protected void emit(EGUIEvent event) {
    listeners.forEach(l -> l.onEvent(event));
  }
}
