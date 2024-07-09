package com.game.engine.render.renderers;

import com.game.caches.GlobalCache;
import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.models.Model;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.MaterialTexturePack;
import com.game.graphics.shaders.Program;
import com.game.graphics.texture.Texture;
import com.game.utils.application.LambdaCounter;
import com.game.utils.enums.EMaterialTexture;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
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
    ArrayBlockingQueue<Entity> queue = scene.renderQueue(type());

    if (queue.isEmpty()) return;

    program.bind();
    while (queue.peek() != null) render(queue.poll(), scene);
    program.unbind();
  }

  public List<Mesh> associate(Model model) {
    return model
      .meshInfo()
      .stream()
      .map(this::associate).toList();
  }

  Mesh associate(MeshInfo info) {
    Mesh mesh = info.create();
    mesh.bind();
    info.vertices().forEach(vertex -> {
      VertexBufferObject vbo = vertex.create(info.vboUsage());
      mesh.vbos().add(vbo);
      List<String> vaas = program.attributes().point(vertex.attributes().values(), vertex.glType());
      mesh.vaas().addAll(vaas);
    });
    if (mesh.isComplex()) {
      IndexBufferObject ibo = new IndexBufferObject();
      mesh.vbos().add(ibo);
      ibo.buffer(info.indices().asIntArray(), info.vboUsage());
    }
    mesh.unbind();
    return mesh;
  }

  protected void draw(Mesh mesh) {
    draw(mesh, GL46.GL_TRIANGLES);
  }

  protected void draw(Mesh mesh, int mode) {
    mesh.bind();
    mesh.vaas().forEach(program.attributes()::enable);
    mesh.draw(mode);
    mesh.vaas().forEach(program.attributes()::disable);
    mesh.unbind();
  }

  protected void setMaterialTextureUniform(MaterialTexturePack textures) {
    program
      .uniforms()
      .set(
        EUniform.MATERIAL_HAS_TEXTURE.value(),
        textures.hasTexture(EMaterialTexture.DIF.getValue())
      );
    program.uniforms()
           .set(
             EUniform.MATERIAL_HAS_NORMAL_MAP.value(),
             textures.hasTexture(EMaterialTexture.NRM.getValue())
           );

    LambdaCounter counter = new LambdaCounter();
    textures.pack().forEach((type, path) -> {
      Texture texture = GlobalCache.instance().texture(path);
      String uniform = EMaterialTexture.getUniformByType(type).value();

      if (texture != null && program.uniforms().has(uniform)) {
        int index = counter.inc();
        int glIndex = GL46.GL_TEXTURE0 + index;
        texture.active(glIndex);
        texture.bind();

        program.uniforms().set(uniform, index);
      }
    });
  }

  public void dispose() {
    program.dispose();
  }
}
