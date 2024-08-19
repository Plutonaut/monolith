package com.game.engine.render.mesh.vertices;

import com.game.utils.application.values.ValueStore;
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

  public VertexInfo(float[] vertices, String attribute, int size, int dimensions, int divisor, int glUsage) {
    AttribInfo attrib = new AttribInfo(attribute, size, dimensions, divisor);
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

  public int totalVertexCount() {
    return vertices.size() * totalDimensions() / totalSize();
  }

  public int totalDimensions() {
    return attributes().values().stream().mapToInt(AttribInfo::dimensions).sum();
  }

  public int totalSize() {
    return attributes.values().stream().mapToInt(AttribInfo::totalSize).sum();
  }

  public AttribInfo getAttribute(String key) {
    return attributes.getOrDefault(key, null);
  }

  public String getAttributeKey() {
    return attributes.keySet().stream().findFirst().orElse(null);
  }

  public boolean hasAttribute(String key) { return getAttribute(key) != null; }

  public void addAttributes(AttribInfo... attributes) {
    for (AttribInfo attribute : attributes) {
      this.attributes.put(attribute.key(), attribute);
    }
  }
}
