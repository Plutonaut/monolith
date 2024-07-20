package com.game.engine.render.mesh;

import com.game.caches.graphics.IGraphicsCachable;
import com.game.engine.render.mesh.vertices.VertexAttributeArray;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EGraphicsCache;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Mesh implements IGraphicsCachable {
  protected final int glId;
  protected final String name;
  protected final List<VertexBufferObject> vbos;
  protected final HashMap<String, VertexAttributeArray> vaas;
//  protected final List<String> vaas;
  protected Material material;
  protected int vertexCount;
  protected boolean isComplex;

  public Mesh(String name) {
    this.name = name;
    glId = GL46.glGenVertexArrays();
    vbos = new ArrayList<>();
    vaas = new HashMap<>();
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

  public void draw(int mode) {
    if (isComplex) drawComplex(mode);
    else drawSimple(mode);
  }

  protected void drawComplex(int mode) {
    drawComplex(mode, vertexCount);
  }

  public void drawComplex(int mode, int vertexCount) {
    GL46.glDrawElements(mode, vertexCount, GL46.GL_UNSIGNED_INT, 0);
  }

  protected void drawSimple(int mode) {
    GL46.glDrawArrays(mode, 0, vertexCount);
  }

  public VertexBufferObject vbo(int glId) {
    return vbos.stream().filter(vbo -> vbo.glId() == glId).findFirst().orElse(null);
  }

  public void setVertexAttributeArrays(List<VertexAttributeArray> vertexAttributeArrays) {
    vertexAttributeArrays.forEach(this::setVertexAttributeArray);
  }

  public void setVertexAttributeArray(VertexAttributeArray vertexAttributeArray) {
    vaas.put(vertexAttributeArray.key(), vertexAttributeArray);
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
