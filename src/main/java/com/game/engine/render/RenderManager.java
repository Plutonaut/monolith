package com.game.engine.render;

import com.game.engine.render.renderers.AbstractRenderer;
import com.game.engine.render.renderers.IRenderer;
import com.game.engine.render.renderers.concrete.*;
import com.game.engine.scene.Scene;
import com.game.utils.enums.ERenderer;

import java.util.HashMap;
import java.util.List;

public class RenderManager implements IRenderer {
  // The order of each renderer matters
  protected static final List<ERenderer> orderedShaderRenderArray = List.of(
    ERenderer.BASIC,
    ERenderer.SKYBOX,
    ERenderer.PARTICLE,
    ERenderer.SPRITE,
    ERenderer.MESH,
    ERenderer.SCENE,
    ERenderer.GUI,
    ERenderer.FONT
  );

  protected final HashMap<ERenderer, AbstractRenderer> renderers;

  public RenderManager() {
    renderers = new HashMap<>();
  }

  protected AbstractRenderer create(ERenderer shader) {
    return switch (shader) {
      case GUI -> new GUIRenderer();
      case MESH -> new MeshRenderer();
      case SCENE -> new SceneRenderer();
      case SKYBOX -> new SkyBoxRenderer();
      case SPRITE -> new SpriteRenderer();
      case FONT -> new FontRenderer();
      case PARTICLE -> new ParticleRenderer();
      case BASIC -> new BasicRenderer();
    };
  }

  AbstractRenderer getRenderer(ERenderer type) {
    return renderers.computeIfAbsent(type, this::create);
  }

  public void render(Scene scene) {
    scene.enter();
    // Order in which objects are rendered dictates placement in scene relative to the view.
    orderedShaderRenderArray.forEach(shader -> {
      // Only bother attempting to render if a render packet exists for this particular shader.
      if (scene.hasPacket(shader)) getRenderer(shader).render(scene);
    });
    scene.exit();
  }

  public void dispose() {
    renderers.values().forEach(AbstractRenderer::dispose);
  }
}
