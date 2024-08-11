package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.renderers.AbstractRenderer;
import com.game.engine.scene.Scene;
import com.game.utils.enums.ERenderer;

public class GUIRenderer extends AbstractRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.GUI;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {

  }
}
