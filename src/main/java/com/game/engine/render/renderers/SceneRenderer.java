package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityAnimationController;
import com.game.engine.scene.lighting.LightingManager;
import com.game.utils.enums.*;
import org.joml.Matrix4f;

public class SceneRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.SCENE;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(EProjection.PERSPECTIVE));
    program.uniforms().set(EUniform.MODEL_VIEW.value(), scene.modelViewMat3D(entity));
    // Possible alternative to the explicit calls being made below
//    entity.modifiers().forEach(mod -> program.uniforms().set(mod.uniform(), entity.isModifierActive(mod)));
    program.uniforms().set(EUniform.SELECTED.value(), entity.isModifierActive(EModifier.SELECTED));
    boolean isAnimated = entity.isModifierActive(EModifier.ANIMATED);
    program.uniforms().set(EUniform.ANIMATED.value(), isAnimated);
    if (isAnimated) {
      EntityAnimationController animController = entity.controllers().animations();
      Matrix4f[] boneMatrices = animController.frameBoneMatrices();
      program.uniforms().set(EUniform.BONE_MATRICES.value(), boneMatrices);
    }
    LightingManager lighting = scene.lighting();
    Matrix4f viewMatrix = scene.camera().view3D();
    setLightingUniforms(lighting, viewMatrix);
    entity.meshes().forEach(mesh -> {
      setMaterialUniform(mesh.material());
      draw(mesh);
    });
  }
}
