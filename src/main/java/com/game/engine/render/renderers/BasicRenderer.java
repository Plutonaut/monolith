package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.utils.enums.ERenderer;

public class BasicRenderer extends AbstractRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.BASIC;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    entity.meshes().forEach(this::draw);
  }
}
