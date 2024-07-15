package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.utils.enums.EMaterialColor;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;

public class SkyBoxRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.SKYBOX;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(EProjection.PERSPECTIVE));
    program.uniforms().set(EUniform.VIEW.value(), scene.camera().view3D());
    program.uniforms().set(EUniform.MODEL.value(), entity.transform().worldModelMat());
    setAmbientLightUniform(scene.lighting().ambientLight());

    entity.meshes().forEach(mesh -> {
      program.uniforms().set(EUniform.MATERIAL_AMBIENT.value(), mesh.material().color(EMaterialColor.AMB.getValue()));
      draw(mesh);
    });
  }
}
