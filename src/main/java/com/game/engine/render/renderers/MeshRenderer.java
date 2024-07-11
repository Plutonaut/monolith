package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;

public class MeshRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.MESH;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    Matrix4f projectionMatrix = scene.projectionMat(EProjection.PERSPECTIVE);
    Matrix4f modelViewMatrix = scene.modelViewMat(entity);
    program.uniforms().set(EUniform.PROJECTION.value(), projectionMatrix);
    program.uniforms().set(EUniform.MODEL_VIEW.value(), modelViewMatrix);

    entity.meshes().forEach(mesh -> {
      Material material = mesh.material();
      setMaterialUniform(material);
      draw(mesh);
    });
  }
}
