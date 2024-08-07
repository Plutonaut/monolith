package com.game.engine.render.renderers;

import com.game.caches.GlobalCache;
import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.shaders.Program;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class AbstractRenderer implements IRenderer {
  protected final Program program;
  protected final List<String> entityIds;

  public AbstractRenderer() {
    program = GlobalCache.instance().program(type().key());
    entityIds = new ArrayList<>();
  }

  public abstract ERenderer type();

  protected abstract void render(IRenderable item, Scene scene);

  public void render(Scene scene) {
    // TODO: renderQueue should return strings, perform lookup in entityManager.
    ArrayBlockingQueue<String> queue = scene.packets().renderQueue(type());
    if (queue.isEmpty()) return;
    program.bind();
    while (queue.peek() != null) {
      String item = queue.poll();
      Entity entity = scene.entity(item);
      scene.parameters().toggleParameters(entity.glParamFlags());
      render(entity, scene);
    }
    program.unbind();
  }

  protected void draw(Mesh mesh) {
    draw(mesh, GL46.GL_TRIANGLES);
  }

  protected void draw(Mesh mesh, int mode) {
    mesh.bind();
    mesh.enable();
    mesh.draw(mode);
    mesh.disable();
    mesh.unbind();
  }

  public void dispose() {
    program.dispose();
  }
}
