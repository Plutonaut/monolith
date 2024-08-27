package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.vertices.TransformFeedbackBuffer;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.particles.ParticleManager;
import com.game.engine.scene.particles.ParticleSpawner;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.lwjgl.opengl.GL46;

public class ParticleRenderer extends AbstractLitRenderer {
  static final int TXT_INDEX = 1;
  boolean initialDraw = true;

  @Override
  public ERenderer type() {
    return ERenderer.PARTICLE;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    ParticleManager pm = scene.particles();

    entity.meshes().forEach(mesh -> {
      ParticleSpawner spawner = pm.spawner(mesh.name());
      if (setSpawnerUniform(spawner)) {
        int vboIndex = spawner.currentVBO();
        int transformIndex = spawner.currentTFB();
        TransformFeedbackBuffer tfbA = mesh.getTFBAtIndex(vboIndex);
        TransformFeedbackBuffer tfbB = mesh.getTFBAtIndex(transformIndex);
        VertexBufferObject vboA = mesh.getVBOByID(tfbA.vboIds().getFirst());
        mesh.bind();
        vboA.bind();
        tfbB.bind();
        vboA.enable();
        vboA.point(program);
        tfbA.begin(GL46.GL_POINTS);
        if (initialDraw) {
          GL46.glDrawArrays(GL46.GL_POINTS, 0, 1);
          initialDraw = false;
        } else tfbA.draw(GL46.GL_POINTS);
        tfbA.end();
        vboA.disable();
        mesh.unbind();
      }
    });
  }

  boolean setSpawnerUniform(ParticleSpawner spawner) {
    if (spawner == null) return false;

    spawner.bindTexture(TXT_INDEX);
    program.uniforms().set(EUniform.TIME.value(), spawner.time());
    program.uniforms().set(EUniform.DELTA_TIME.value(), spawner.deltaTime());

    program.uniforms().set(EUniform.RANDOM_SAMPLER.value(), TXT_INDEX);
    if (initialDraw) {
      program.uniforms().set(EUniform.LAUNCHER_LIFETIME.value(), spawner.launcherLifetime());
      program.uniforms().set(EUniform.SHELL_LIFETIME.value(), spawner.shellLifetime());
      program.uniforms().set(EUniform.FRAG_LIFETIME.value(), spawner.fragLifetime());
    }

    return true;
  }
}
