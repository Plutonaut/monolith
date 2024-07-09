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

  public VertexInfo(float[] vertices, String attribute) {
    this(vertices, attribute, 3);
  }

  public VertexInfo(float[] vertices, String attribute, int size) {
    this(vertices, attribute, size, 1);
  }

  public VertexInfo(float[] vertices, String attribute, int size, int instances) {
    AttribInfo attrib = new AttribInfo(attribute, size, instances);
    ValueStore store = new ValueStore();
    store.add(vertices);

    this.glType = GL46.GL_FLOAT;
    this.vertices = store;
    attributes = new HashMap<>();
    addAttributes(attrib);
  }

  public VertexInfo(int[] vertices, String attribute, int size) {
    this(vertices, attribute, size, 1);
  }

  public VertexInfo(int[] vertices, String attribute, int size, int instances) {
    AttribInfo attrib = new AttribInfo(attribute, size, instances);
    ValueStore store = new ValueStore();
    store.add(vertices);

    this.glType = GL46.GL_INT;
    this.vertices = store;
    attributes = new HashMap<>();
    addAttributes(attrib);
  }

  public VertexInfo(ValueStore vertices, int glType, AttribInfo... attributes) {
    this.glType = glType;
    this.attributes = new HashMap<>();
    this.vertices = vertices;

    addAttributes(attributes);
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

  public VertexBufferObject create(int usage) {
    VertexBufferObject vbo = new VertexBufferObject();
    if (glType == GL46.GL_INT)
      vbo.buffer(vertices.asIntArray(), usage);
    else
      vbo.buffer(vertices.asArray(), usage);
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
