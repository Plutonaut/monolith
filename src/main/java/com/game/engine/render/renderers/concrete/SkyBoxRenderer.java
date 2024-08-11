package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;

public class SkyBoxRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.SKYBOX;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(entity.projection()));

    // Entity does not move relative to the observer
    Matrix4f stationaryView = new Matrix4f().set(scene.camera().view3D()).m30(0).m31(0).m32(0).mul(entity.transform().worldModelMat());
    program.uniforms().set(EUniform.MODEL_VIEW.value(), stationaryView);
    setAmbientLightUniform(scene.lighting().ambientLight());

    entity.meshes().forEach(mesh -> {
      Material material = mesh.material();
      setMaterialUniform(material);
      mesh.render();
    });
  }
}
