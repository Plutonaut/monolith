package com.game.engine.physics;

import com.game.utils.logging.PrettifyUtils;
import com.game.utils.math.VectorUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
@Slf4j
public class Bounds3D {
  protected Vector3f origin;
  protected Vector3f min;
  protected Vector3f max;

  public Bounds3D() {
    origin = new Vector3f();
    min = new Vector3f();
    max = new Vector3f();
  }

  public boolean contains(Vector3f position) {
    log.info("Bounds: {}", this);
    return VectorUtils.lessThan(minVertex(), position) && VectorUtils.greaterThan(
      maxVertex(),
      position
    );
  }

  public void set(Bounds3D bounds) {
    this.origin.set(bounds.origin);
    this.min.set(bounds.min);
    this.max.set(bounds.max);
  }

  public boolean above(Vector3f position) {
    return minVertex().x() < position.x() && minVertex().z() < position.z() && maxVertex().x() > position.x() && maxVertex().z() > position.z() && minVertex().y() < position.y();
  }

  public void scale(float scale) {
    min.mul(scale);
    max.mul(scale);
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

  @Override
  public String toString() {
    return "Bounds 3D" +
      "\nOrigin: " + PrettifyUtils.prettify(origin) +
      " Min Vertex: " + PrettifyUtils.prettify(minVertex()) +
      " Max Vertex: " + PrettifyUtils.prettify(maxVertex());
  }
}
