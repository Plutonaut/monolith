package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;

public class MeshRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.MESH;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(entity.projection()));
    program.uniforms().set(EUniform.MODEL_VIEW.value(), scene.modelViewMat3D(entity));

    entity.meshes().forEach(mesh -> {
      Material material = mesh.material();
      setMaterialUniform(material);
      mesh.render();
    });
  }
}
