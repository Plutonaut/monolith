package com.game.engine.render;

import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.PacketResult;
import com.game.engine.render.renderers.AbstractRenderer;
import com.game.engine.render.renderers.IRenderer;
import com.game.engine.scene.Scene;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

import java.util.HashMap;
import java.util.List;

public class RenderManager implements IRenderer {
  // The order of each renderer matters
  protected static final List<ERenderer> orderedShaderRenderArray = List.of(
    ERenderer.BASIC,
    ERenderer.FONT,
    ERenderer.SKYBOX,
    ERenderer.SPRITE,
    ERenderer.MESH,
    ERenderer.SCENE
  );

  protected final HashMap<ERenderer, AbstractRenderer> renderers;
  protected final RendererFactory factory;

  public RenderManager() {
    renderers = new HashMap<>();
    factory = new RendererFactory();
  }

  AbstractRenderer getRenderer(ERenderer type) {
    return renderers.computeIfAbsent(type, factory::create);
  }

  PacketResult bind(ERenderer shader, Model model) {
    return getRenderer(shader).associate(model);
  }

  // TODO: Move entity creation to renderer. Add newly created entities to global cache. Pull from global cache on render.
  public void bind(Scene scene) {
    scene.packets().bindQueue(this::bind);
  }

  public void render(Scene scene) {
    scene.enter();
    orderedShaderRenderArray.forEach(shader -> {
      // Only bother attempting to render if a renderer object already exists.
      if (renderers.containsKey(shader)) getRenderer(shader).render(scene);
    });
    scene.exit();
  }

  // Support for culling back faces
  public void cull(boolean enabled) {
    toggleGl(GL46.GL_CULL_FACE, enabled);
    GL46.glCullFace(GL46.GL_BACK);
  }

  public void wireframe(boolean enabled) {
    int mode = enabled ? GL46.GL_LINE : GL46.GL_FILL;
    GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, mode);
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
