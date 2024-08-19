package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.InstancedMesh;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityAnimationController;
import com.game.engine.scene.lighting.LightingManager;
import com.game.utils.enums.EModifier;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;

public class SceneRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.SCENE;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    Matrix4f viewMatrix = scene.camera().view3D();
    boolean isAnimated = entity.isModifierActive(EModifier.ANIMATED);

    program.uniforms().set(EUniform.PROJECTION.value(), scene.projectionMat(entity.projection()));
    program.uniforms().set(EUniform.VIEW.value(), viewMatrix);
    program.uniforms().set(EUniform.MODEL.value(), entity.transform().worldModelMat());
//    program.uniforms().set(EUniform.MODEL_VIEW.value(), scene.modelViewMat3D(entity));
    // Possible alternative to the explicit calls being made below
//    entity.modifiers().forEach(mod -> program.uniforms().set(mod.uniform(), entity.isModifierActive(mod)));
//    program.uniforms().set(EUniform.SELECTED.value(), entity.isModifierActive(EModifier.SELECTED));
    program.uniforms().set(EUniform.ANIMATED.value(), isAnimated);
    if (isAnimated) {
      EntityAnimationController animController = entity.controllers().animations();
      Matrix4f[] boneMatrices = animController.frameBoneMatrices();
      program.uniforms().set(EUniform.BONE_MATRICES.value(), boneMatrices);
    }
    LightingManager lighting = scene.lighting();
    setLightingUniforms(lighting, viewMatrix);
    entity.meshes().forEach(mesh -> {
      setMaterialUniform(mesh.material());
      program.uniforms().set(EUniform.SELECTED.value(), mesh.selected());
      program.uniforms().set(EUniform.IS_INSTANCED.value(), mesh instanceof InstancedMesh);
      mesh.render();
    });
  }
}
