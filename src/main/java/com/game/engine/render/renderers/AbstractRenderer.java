package com.game.engine.render.renderers;

import com.game.caches.GlobalCache;
import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.EntityPacketResult;
import com.game.engine.render.pipeline.packets.PacketResult;
import com.game.engine.scene.Scene;
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
    ArrayBlockingQueue<IRenderable> queue = scene.packets().renderQueue(type());
    if (queue.isEmpty()) return;
    program.bind();
    while (queue.peek() != null) render(queue.poll(), scene);
    program.unbind();
  }

  public PacketResult associate(Model model) {
    EntityPacketResult result = new EntityPacketResult();
    model.meshInfo().forEach(info -> result.addMesh(associate(info)));
    return result;
  }

  Mesh associate(MeshInfo info) {
    Mesh mesh = info.create();
    mesh.bind();
    info.vertices().forEach(vertex -> {
      mesh.vbos().add(vertex.create());
      mesh.setVertexAttributeArrays(program.attributes().point(vertex));
    });
    if (mesh.isComplex()) {
      IndexBufferObject ibo = new IndexBufferObject();
      mesh.vbos().add(ibo);
      ibo.buffer(info.indices().asIntArray());
    }
    mesh.unbind();
    return mesh;
  }

  protected void draw(Mesh mesh) {
    draw(mesh, GL46.GL_TRIANGLES);
  }

  protected void draw(Mesh mesh, int mode) {
    mesh.bind();
    mesh.vaas().values().forEach(program.attributes()::enable);
    mesh.draw(mode);
    mesh.vaas().values().forEach(program.attributes()::disable);
    mesh.unbind();
  }

  public void dispose() {
    program.dispose();
  }
}
