package com.game.engine.physics;

import com.game.utils.logging.PrettifyUtils;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import org.joml.Vector2f;

@AllArgsConstructor
@Accessors(fluent = true)
@Value
public class Bounds2D {
  Vector2f origin;
  Vector2f min;
  Vector2f max;

  public Bounds2D() {
    origin = new Vector2f();
    min = new Vector2f();
    max = new Vector2f();
  }

  public Vector2f size() {
    return new Vector2f(max).sub(min);
  }

  public Vector2f minVertex() {
    return new Vector2f(min).add(origin);
  }

  public Vector2f maxVertex() {
    return new Vector2f(max).add(origin);
  }

  public void set(Bounds2D bounds) {
    this.origin.set(bounds.origin());
    this.min.set(bounds.min());
    this.max.set(bounds.max());
  }

  @Override
  public String toString() {
    return "Bounds 2D" + "\nOrigin: " + PrettifyUtils.prettify(origin) + " Min Vertex: " + PrettifyUtils.prettify(
      minVertex()) + " Max Vertex: " + PrettifyUtils.prettify(maxVertex());
  }
}
