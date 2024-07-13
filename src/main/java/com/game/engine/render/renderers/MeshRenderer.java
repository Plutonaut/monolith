package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EProjection;
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
    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(EProjection.PERSPECTIVE));
    program.uniforms().set(EUniform.MODEL_VIEW.value(), scene.modelViewMat(entity));

    entity.meshes().forEach(mesh -> {
      Material material = mesh.material();
      setMaterialUniform(material);
      draw(mesh);
    });
  }
}
