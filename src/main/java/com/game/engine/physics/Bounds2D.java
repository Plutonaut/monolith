package com.game.engine.physics;

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
}
