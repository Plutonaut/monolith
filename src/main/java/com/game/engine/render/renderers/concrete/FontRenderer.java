package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityTextController;
import com.game.graphics.materials.Material;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;

public class FontRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() { return ERenderer.FONT; }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    EntityTextController text = entity.controllers().text();
    program.uniforms().set(EUniform.PROJECTION.value(), scene.modelProjectionMat(entity));
    DynamicMesh mesh = text.mesh();
    Material material = mesh.material();
    setMaterialUniform(material);
    draw(mesh);
  }
}
