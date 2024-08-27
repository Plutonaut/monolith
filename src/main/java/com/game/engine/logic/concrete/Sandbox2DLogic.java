package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.physics.Bounds2D;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityInteractionController;
import com.game.engine.settings.EngineSettings;
import com.game.utils.enums.EGUIEvent;
import com.game.utils.enums.ESprite;
import org.lwjgl.glfw.GLFW;

public class Sandbox2DLogic extends AbstractLogic {
  Entity iggySprite;
  Entity textEntity;
  EntityInteractionController textInteractionController;
  Bounds2D bounds;
  boolean iggyIsBound = false;

  public Sandbox2DLogic(EngineSettings settings) {
    super(settings);
  }

  void handleMouseInput(EGUIEvent event) {
    if (iggyIsBound) return;

    switch (event) {
      case EGUIEvent.ENTER -> textEntity.redrawText("Prepare for his arrival");
      case EGUIEvent.EXIT -> textEntity.redrawText("COWARD!");
    }
  }

  void handleLeftMouseClick(int action) {
    if (action == GLFW.GLFW_PRESS && textInteractionController.isHover()) {
      scene.bind(iggySprite);
      iggyIsBound = true;
    }
  }

  @Override
  protected String windowTitle() {
    return "2D Sprite Sandbox";
  }

  @Override
  public void onStart() {
    textEntity = scene.createText("test", "Click and he shall appear...").move2D(25, 25);
    textInteractionController = textEntity.controllers().interaction();
    textInteractionController.listen(this::handleMouseInput);
    bounds = textEntity.controllers().text().bounds();

    iggySprite = scene.createSprite(ESprite.IGGY.atlasName(), ESprite.IGGY.path(), true).scale(5);

    scene.window().mouse().addListener(GLFW.GLFW_MOUSE_BUTTON_1, this::handleLeftMouseClick);
    scene.bind(textEntity);
  }

  @Override
  public void input() {
    textInteractionController.onMouseInput(scene.window().mouse(), bounds);
  }

  @Override
  public void update(float interval) {
  }
}
