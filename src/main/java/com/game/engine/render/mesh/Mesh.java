package com.game.engine.render.mesh;

import com.game.engine.render.mesh.vertices.VertexAttributeArray;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.graphics.IGraphics;
import com.game.graphics.materials.Material;
import com.game.utils.enums.ECache;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import java.util.HashSet;

@Accessors(fluent = true)
@Data
public class Mesh implements IGraphics {
  protected final int glId;
  protected final String name;
  protected final HashSet<VertexBufferObject> vbos;
  protected final HashSet<VertexAttributeArray> vaas;
  protected Material material;
  protected Vector3f min;
  protected Vector3f max;
  protected int drawMode;
  protected int vertexCount;
  protected boolean isComplex;

  public Mesh(String name) {
    this.name = name;
    glId = GL46.glGenVertexArrays();
    vbos = new HashSet<>();
    vaas = new HashSet<>();
    min = new Vector3f();
    max = new Vector3f();
    drawMode = GL46.GL_TRIANGLES;
  }

  @Override
  public ECache type() { return ECache.MESH; }

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

  public void render() {
    bind();
    enable();
    draw(drawMode);
    disable();
    unbind();
  }

  protected void draw(int mode) {
    if (isComplex) drawComplex(mode);
    else drawSimple(mode);
  }

  protected void drawComplex(int mode) {
    drawComplex(mode, vertexCount);
  }

  protected void drawComplex(int mode, int vertexCount) {
    GL46.glDrawElements(mode, vertexCount, GL46.GL_UNSIGNED_INT, 0);
  }

  protected void drawSimple(int mode) {
    GL46.glDrawArrays(mode, 0, vertexCount);
  }

  public void addVertexBufferObject(VertexBufferObject vertexBufferObject) {
    vbos.add(vertexBufferObject);
  }

  public void setVertexAttributeArray(VertexAttributeArray vertexAttributeArray) {
    vaas.add(vertexAttributeArray);
  }

  public void enable() {
    vaas.forEach(VertexAttributeArray::enable);
  }

  public void disable() {
    vaas.forEach(VertexAttributeArray::disable);
  }

  public void updateBounds(Vector3f min, Vector3f max) {
    this.min.set(min);
    this.max.set(max);
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
