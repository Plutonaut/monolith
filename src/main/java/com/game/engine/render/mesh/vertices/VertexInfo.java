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
  protected String customName;
  protected int transformFeedback = -1;

  public VertexInfo(
    float[] vertices, String attribute, int size, int dimensions, int divisor, int glUsage
  ) {
    AttribInfo attrib = new AttribInfo(attribute, size, dimensions, divisor);
    this.vertices = new ValueStore();
    this.vertices.add(vertices);
    this.glUsage = glUsage;
    this.customName = null;

    glType = GL46.GL_FLOAT;
    attributes = new HashMap<>();
    addAttributes(attrib);
  }

  public VertexInfo(ValueStore vertices, int glType, int glUsage, AttribInfo... attributes) {
    this.glUsage = glUsage;
    this.glType = glType;
    this.vertices = vertices;

    this.attributes = new HashMap<>();
    this.customName = null;
    addAttributes(attributes);
  }

  public float[] getAttributeVertex(int i, String attribute) {
    AttribInfo info = getAttribute(attribute);
    int vertexSize = totalSize();
    int attrSize = info.size();
    float[] result = new float[attrSize];
    int index = vertexSize * i;
    for (AttribInfo value : attributes.values()) {
      if (value.key.equalsIgnoreCase(info.key())) {
        float[] vertexArr = vertices.asArray();
        try {
          System.arraycopy(vertexArr, index, result, 0, attrSize);
        } catch (IndexOutOfBoundsException e) {
          String str = "Index out of bounds" + e.getMessage() + "\nAttribute: " + attribute +
            "\nParam index: " + i + "\nCurrent index: " + index +
            "\nTotal size: " + totalSize() + "\nTotal vertex count: " +
            totalVertexCount() + "\nTotal value count: " + vertexArr.length;
          throw new IllegalStateException(str);
        }
        break;
      }
      index += value.size();
    }
    return result;
  }

  public int totalVertexCount() {
    return vertices.size() * totalDimensions() / totalSize();
  }

  public int totalDimensions() {
    return attributes().values().stream().mapToInt(AttribInfo::dimensions).max().orElse(1);
  }

  public int totalSize() {
    return attributes.values().stream().mapToInt(AttribInfo::totalSize).sum();
  }

  public AttribInfo getAttribute(String key) {
    return attributes.getOrDefault(key, null);
  }

  public String getAttributeKey() {
    return customName == null ? attributes.keySet().stream().findFirst().orElse(null) : customName;
  }

  public boolean hasAttribute(String key) { return getAttribute(key) != null; }

  public void addAttributes(AttribInfo... attributes) {
    for (AttribInfo attribute : attributes) {
      this.attributes.put(attribute.key(), attribute);
    }
  }
}
