package com.game.engine.render;

import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.RenderPacket;
import com.game.engine.render.renderers.AbstractRenderer;
import com.game.engine.render.renderers.IRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

import java.util.HashMap;
import java.util.List;

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

  public RenderPacket bind(ERenderer shader, Model... models) {
    RenderPacket packet = new RenderPacket(shader);
    AbstractRenderer renderer = getRenderer(packet.destination());
//    ArrayBlockingQueue<Model> queue = new ArrayBlockingQueue<>(16);
    for (Model model : models) {
      List<Mesh> meshes = renderer.associate(model);
      Entity entity = model.create(model.name() + "_" + packet.destination());
      entity.addMeshes(meshes);
      packet.add(entity);
    }
    return packet;
  }

  void bind(ERenderer shader, RenderPacket packet) {
    AbstractRenderer renderer = getRenderer(packet.destination());
    while (packet.queue().peek() != null) {
      Model model = packet.queue().poll();
      List<Mesh> meshes = renderer.associate(model);
      Entity entity = model.create(model.name() + "_" + packet.destination());
      entity.addMeshes(meshes);
      packet.add(entity);
    }
  }

  public void bind(Scene scene) {
    scene.packets().forEach(this::bind);
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
