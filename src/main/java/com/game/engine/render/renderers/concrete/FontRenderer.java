package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityTextController;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;

public class FontRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() { return ERenderer.FONT; }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    EntityTextController text = entity.controllers().text();
    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(EProjection.ORTHOGRAPHIC_FONT_2D));
    program.uniforms().set(EUniform.MODEL.value(), entity.transform().worldModelMat());

    DynamicMesh textMesh = text.mesh();
    Material textMaterial = textMesh.material();
    setMaterialUniform(textMaterial);
    textMesh.render();

    entity.meshes().forEach(mesh -> {
      Material material = mesh.material();
      setMaterialUniform(material);
      mesh.render();
    });
  }
}
