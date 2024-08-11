package com.game.engine.physics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
public class Bounds3D {
  protected Vector3f origin;
  protected Vector3f min;
  protected Vector3f max;

  public Bounds3D() {
    origin = new Vector3f();
    min = new Vector3f();
    max = new Vector3f();
  }

  public Vector3f size() {
    return new Vector3f(max).sub(min);
  }

  public Vector3f minVertex() {
    return new Vector3f(min).add(origin);
  }

  public Vector3f maxVertex() {
    return new Vector3f(max).add(origin);
  }
}
