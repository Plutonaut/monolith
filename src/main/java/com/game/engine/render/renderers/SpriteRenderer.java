package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;

public class SpriteRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.SPRITE;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    EProjection projectionType = EProjection.ORTHOGRAPHIC_FONT_2D;
    Entity entity = (Entity) item;
    Matrix4f mat = new Matrix4f()
      .set(scene.projectionMat(projectionType))
      .mul(scene.modelViewMat2D(entity));
    program.uniforms().set(EUniform.PROJECTION.value(), mat);
    entity.meshes().forEach(mesh -> {
      Material material = mesh.material();
      setMaterialUniform(material);
      draw(mesh);
    });
  }
}
