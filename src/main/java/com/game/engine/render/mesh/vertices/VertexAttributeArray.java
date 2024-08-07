package com.game.engine.render.mesh.vertices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
public class VertexAttributeArray {
  private final HashSet<VertexAttribute> attributes;
  private final int stride;
  private final int glType;

  public VertexAttributeArray(int glType, int stride) {
    attributes = new HashSet<>();
    this.stride = stride;
    this.glType = glType;
  }

  public void addVertexAttribute(
    String key,
    int location,
    int size,
    int offset,
    int divisor
  ) {
    VertexAttribute attribute = new VertexAttribute(
      key,
      location,
      size,
      offset,
      divisor
    );
    attribute.point(stride, glType);
    attributes.add(attribute);
  }

  public void enable() {
    attributes.forEach(VertexAttribute::enable);
  }

  public void disable() {
    attributes.forEach(VertexAttribute::disable);
  }
}
