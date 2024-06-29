package com.game.engine.render.mesh.vertices;

import com.game.utils.application.ValueStore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true)
@Data
public class VertexInfo {
  protected final HashMap<String, AttribInfo> attributes;
  protected final ValueStore vertices;
  protected final int glType;

  public VertexInfo(ValueStore vertices, int glType, AttribInfo... attributes) {
    this.glType = glType;
    this.attributes = new HashMap<>();
    this.vertices = vertices;

    addAttributes(attributes);
  }

  public int numVertices() {return vertices.size() / totalAttributeSize();}

  public void addVertices(float... values) {
    this.vertices.add(values);
  }

  public void addVertices(List<Float> values) {
    this.vertices.add(values.stream());
  }

  public void addVertices(ValueStore values) {
    this.vertices.add(values);
  }

  public AttribInfo getAttribute(String key) {
    return attributes.getOrDefault(key, null);
  }

  public void addAttributes(AttribInfo... attributes) {
    for (AttribInfo attribute : attributes) {
      this.attributes.put(attribute.key(), attribute);
    }
  }

  int totalAttributeSize() {
    return attributes.values().stream().mapToInt(a -> a.size).sum();
  }
}
