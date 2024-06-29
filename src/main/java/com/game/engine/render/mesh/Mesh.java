package com.game.engine.render.mesh;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.graphics.IGraphicsCachable;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.utils.application.ValueStore;
import com.game.utils.enums.EGraphicsCache;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Mesh implements IGraphicsCachable {
  protected final int glId;
  protected final String name;
  protected final List<VertexBufferObject> vbos;
  protected Material material;

  public Mesh(String name) {
    this.name = name;
    glId = GL46.glGenVertexArrays();
    vbos = new ArrayList<>();
  }

  @Override
  public EGraphicsCache type() { return EGraphicsCache.MESH; }

  @Override
  public String key() {
    return name;
  }

  @Override
  public void bind() {
    GL46.glBindVertexArray(glId);
  }

  @Override
  public void dispose() {
    unbind();
    GL46.glDeleteVertexArrays(glId);
    vbos.forEach(VertexBufferObject::dispose);
  }

  public MeshInfo info() {
    return GlobalCache.instance().meshInfo(name);
  }

  public void draw(int mode) {
    MeshInfo info = info();
    if (info.isComplex()) complexDraw(mode);
    else simpleDraw(mode);
  }

  void complexDraw(int mode) {
    MeshInfo info = info();
    if (info.isInstanced()) GL46.glDrawElementsInstanced(
      mode,
      info.vertexCount(),
      GL46.GL_UNSIGNED_INT,
      0,
      info.instances()
    );
    else complexDraw(mode, info.vertexCount());
  }

  public void complexDraw(int mode, int vertexCount) {
    GL46.glDrawElements(mode, vertexCount, GL46.GL_UNSIGNED_INT, 0);
  }

  void simpleDraw(int mode) {
    MeshInfo info = info();
    if (info.isInstanced())
      GL46.glDrawArraysInstanced(mode, 0, info.vertexCount(), info.instances());
    else GL46.glDrawArrays(mode, 0, info.vertexCount());
  }

  public void attach(ERenderer shader) {
    Program program = GlobalCache.instance().program(shader.key());
    program.bind();
    attach(program);
    program.unbind();
  }
  
  public void attach(Program program) {
    bind();

    MeshInfo info = info();
    info.vertices().forEach(vertex -> {
      bufferf(vertex);
      program.point(vertex.attributes().values(), vertex.glType());
    });
    if (info.isComplex()) {
      bufferidx(info.indices());
    }

    unbind();
  }

  void bufferidx(ValueStore values) {
    IndexBufferObject idxbo = new IndexBufferObject();
    bufferi(idxbo, values);
    vbos().add(idxbo);
  }

  void bufferi(VertexBufferObject vbo, ValueStore values) {
    IntBuffer buffer = MemoryUtil.memAllocInt(values.size()).put(values.asIntArray()).flip();
    vbo.bind();
    vbo.upload(buffer, GL46.GL_STATIC_DRAW);
    MemoryUtil.memFree(buffer);
  }

  void bufferf(VertexInfo info) {
    ValueStore values = info.vertices();
    VertexBufferObject vbo = new VertexBufferObject();

    if (info.glType() == GL46.GL_INT) {
      bufferi(vbo, values);
    } else {
      FloatBuffer buffer = MemoryUtil.memAllocFloat(values.size()).put(values.asArray()).flip();
      vbo.bind();
      vbo.upload(buffer, GL46.GL_STATIC_DRAW);
      MemoryUtil.memFree(buffer);
    }

    vbos().add(vbo);
  }

  public void unbind() {
    // unbind textures
    GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    // unbind buffers
    GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
    // unbind mesh
    GL46.glBindVertexArray(0);
  }
}
