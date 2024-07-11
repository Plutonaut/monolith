package com.game.engine.render.mesh.vertices;

import com.game.utils.application.ValueStore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class VertexInfo {
  protected final HashMap<String, AttribInfo> attributes;
  protected final ValueStore vertices;
  protected final int glType;
  protected final int glUsage;

  public VertexInfo(float[] vertices, String attribute, int size, int instances, int glUsage) {
    AttribInfo attrib = new AttribInfo(attribute, size, instances);
    this.vertices = new ValueStore();
    this.vertices.add(vertices);
    this.glUsage = glUsage;

    glType = GL46.GL_FLOAT;
    attributes = new HashMap<>();
    addAttributes(attrib);
  }

  public VertexInfo(ValueStore vertices, int glType, int glUsage, AttribInfo... attributes) {
    this.glUsage = glUsage;
    this.glType = glType;
    this.vertices = vertices;

    this.attributes = new HashMap<>();
    addAttributes(attributes);
  }

  public int stride() {
    return attributes.size() == 1 ? 0 : totalSize();
  }

  public int totalVertexCount() {
    return vertices.size() * maxInstances() / totalSize();
  }

  public int maxInstances() {
    return attributes().values().stream().mapToInt(AttribInfo::instances).max().orElse(1);
  }

  public int totalSize() {
    return attributes.values().stream().mapToInt(AttribInfo::size).sum();
  }

  public VertexBufferObject create() {
    VertexBufferObject vbo = new VertexBufferObject();
    if (glType == GL46.GL_INT)
      vbo.buffer(vertices.asIntArray(), glUsage);
    else
      vbo.buffer(vertices.asArray(), glUsage);
    return vbo;
  }

  public AttribInfo getAttribute(String key) {
    return attributes.getOrDefault(key, null);
  }

  public boolean hasAttribute(String key) { return getAttribute(key) != null; }

  public void addAttributes(AttribInfo... attributes) {
    for (AttribInfo attribute : attributes) {
      this.attributes.put(attribute.key(), attribute);
    }
  }
}
