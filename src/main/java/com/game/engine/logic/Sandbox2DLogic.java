package com.game.engine.logic;

import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.enums.EModel;
import com.game.utils.enums.ESprite;
import org.lwjgl.glfw.GLFW;

public class Sandbox2DLogic extends AbstractLogic {
  Entity iggySprite;
  Entity textEntity;

  public Sandbox2DLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  protected String windowTitle() {
    return "2D Sprite Sandbox";
  }

  @Override
  public void onStart() {
    textEntity = scene.createText("test", "Wait for it...");
    textEntity.move(25, 25);

    iggySprite = scene.createSprite(ESprite.IGGY.atlasName(), ESprite.IGGY.path());
    iggySprite.scale(50f).move(scene.window().width() / 2f, scene.window().height() / 2f);

    Entity skyBox = scene.createSkyBox("sky", EModel.BASIC_SKYBOX.path());
    skyBox.scale(25f);

    scene.bind(textEntity);
    scene.window().keyboard().onKeyPress(GLFW.GLFW_KEY_E, (action) -> {
      textEntity.redrawText("Tada~");
      scene.bind(iggySprite);
    });

    scene.window().keyboard().onKeyPress(GLFW.GLFW_KEY_Q, (action) -> {
      textEntity.redrawText("Wait for it...");
      scene.unbind(iggySprite);
    });
  }

  @Override
  public void input() {
  }

  @Override
  public void update(float interval) {

  }
}
