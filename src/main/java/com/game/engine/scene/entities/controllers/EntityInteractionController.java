package com.game.engine.scene.entities.controllers;

import com.game.engine.controls.Mouse;
import com.game.engine.physics.Bounds2D;
import com.game.engine.scene.entities.IEntityInteractionCallback;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.engine.PhysicsUtils;
import com.game.utils.enums.EController;
import com.game.utils.enums.EGUIEvent;
import lombok.Getter;

public class EntityInteractionController extends AbstractEntityController {
  protected IEntityInteractionCallback listener;
  @Getter
  protected boolean hover;
  protected int leftMouse;
  protected int rightMouse;

  public EntityInteractionController() {
    listener = null;
    leftMouse = -1;
    rightMouse = -1;
  }

  @Override
  public String type() {
    return EController.INTERACTION.value();
  }

  @Override
  public void onUpdate(ModelTransform transform) {

  }

  public void listen(IEntityInteractionCallback listener) {
    this.listener = listener;
  }

  public void onMouseInput(Mouse mouse, Bounds2D bounds) {
    boolean isInside = PhysicsUtils.isPointInsideBounds2D(mouse.position(), bounds);

    if (isInside != hover) {
      hover = isInside;
      emit(isInside ? EGUIEvent.ENTER : EGUIEvent.EXIT);
    }
  }

  protected void emit(EGUIEvent event) {
    listener.onEvent(event);
  }
}
