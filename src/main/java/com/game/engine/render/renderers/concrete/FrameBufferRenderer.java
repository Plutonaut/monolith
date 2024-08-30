package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.renderers.AbstractLitRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

public class FrameBufferRenderer extends AbstractLitRenderer {

  @Override
  public ERenderer type() {
    return ERenderer.FRAMEBUFFER;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;

    entity.meshes().forEach(mesh -> {
      GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
      Material material = mesh.material();
      setMaterialUniform(material);
      mesh.render();
    });
  }
}
