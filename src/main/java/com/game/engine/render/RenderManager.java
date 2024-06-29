package com.game.engine.render;

import com.game.engine.render.renderers.AbstractRenderer;
import com.game.engine.render.renderers.IRenderer;
import com.game.engine.scene.Scene;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

import java.util.HashMap;

public class RenderManager implements IRenderer {
  protected final HashMap<ERenderer, AbstractRenderer> renderers;
  protected final RendererFactory factory;

  public RenderManager() {
    renderers = new HashMap<>();
    factory = new RendererFactory();
  }

  AbstractRenderer getRenderer(ERenderer type) {
    return renderers.computeIfAbsent(type, factory::create);
  }

  public void init(ERenderer... shaders) {
    for (ERenderer shader : shaders)
      renderers.putIfAbsent(shader, factory.create(shader));
  }

  public void render(Scene scene) {
    scene.enter();
    renderers.values().forEach(renderer -> renderer.render(scene));
    scene.exit();
  }

  // Support for culling back faces
  public void cull(boolean enabled) {
    toggleGl(GL46.GL_CULL_FACE, enabled);
    GL46.glCullFace(GL46.GL_BACK);
  }

  public void depth(boolean enabled) {
    toggleGl(GL46.GL_DEPTH_TEST, enabled);
  }

  // Support for transparencies
  public void blend(boolean enabled) {
    toggleGl(GL46.GL_BLEND, enabled);
    GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
  }

  public void clearColor(float red, float green, float blue, float alpha) {
    GL46.glClearColor(red, green, blue, alpha);
  }

  private void toggleGl(int target, boolean toggle) {
    if (toggle) GL46.glEnable(target);
    else GL46.glDisable(target);
  }

  public void dispose() {
    renderers.values().forEach(AbstractRenderer::dispose);
  }
}
