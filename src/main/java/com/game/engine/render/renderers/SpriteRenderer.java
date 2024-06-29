package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;

public class SpriteRenderer extends AbstractRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.SPRITE;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    program.uniforms().set(EUniform.PROJECTION.value(), scene.modelOrthoMat(entity));

    entity.meshes().forEach(mesh -> {
      setMaterialTextureUniform(mesh.material().textures());
      draw(mesh);
    });
  }
}
