package com.game.engine.render.mesh.vertices;

import com.game.graphics.shaders.Program;
import com.game.utils.application.LambdaCounter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.stream.Stream;

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

  public void point(Program program) {
    keys().forEach(key -> point(key, program));
  }

  public void point(String key, Program program) {
    int l = program.attributes().get(key);
    LambdaCounter location = new LambdaCounter(l);
    if (location.value() < 0) return;

    get(key).forEach((vertexAttribute) -> vertexAttribute.point(stride, glType, location.inc()));
  }

  public Stream<VertexAttribute> get(String key) {
    return attributes.stream().filter(a -> a.key().equalsIgnoreCase(key));
  }

  public String key() { return keys().findFirst().orElse(null); }

  public Stream<String> keys() { return attributes.stream().map(VertexAttribute::key).distinct(); }

  public void enable() {
    keys().forEach(this::enable);
  }

  public void enable(String key) { get(key).forEach(VertexAttribute::enable); }

  public void disable() {
    keys().forEach(this::disable);
  }

  public void disable(String key) {get(key).forEach(VertexAttribute::disable); }
}
