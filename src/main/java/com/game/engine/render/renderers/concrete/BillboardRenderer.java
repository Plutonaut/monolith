package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.vertices.TransformFeedbackBuffer;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.particles.ParticleManager;
import com.game.engine.scene.particles.ParticleSpawner;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EAttribute;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.lwjgl.opengl.GL46;

public class BillboardRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() {
    return ERenderer.BILLBOARD;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    program.uniforms().set(EUniform.VIEW_PROJECTION.value(), scene.viewProjectionMat3D(entity));
    program.uniforms().set(EUniform.CAMERA_POS.value(), scene.camera().position());
    program.uniforms().set(EUniform.BILLBOARD_SIZE.value(), entity.transform().scale());
    ParticleManager pm = scene.particles();
    entity.meshes().forEach(mesh -> {
      Material material = mesh.material();
      if (material != null) setMaterialUniform(material);
      ParticleSpawner spawner = pm.spawner(mesh.name());
      if (spawner == null) mesh.render();
      else {
        mesh.bind();
        int tfbIndex = spawner.currentTFB();
        TransformFeedbackBuffer tfb = mesh.getTFBAtIndex(tfbIndex);
        int vboId = tfb.vboIds().getFirst();
        VertexBufferObject vbo = mesh.getVBOByID(vboId);
        vbo.bind();
        GL46.glEnableVertexAttribArray(0);
        vbo.point(EAttribute.POS.getValue(), program);
        tfb.draw(GL46.GL_POINTS);
        GL46.glDisableVertexAttribArray(0);
        mesh.unbind();
      }
    });
  }
}
