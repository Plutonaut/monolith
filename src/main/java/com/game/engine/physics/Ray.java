package com.game.engine.physics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector2f;
import org.joml.Vector3f;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
public class Ray {
  protected Vector3f origin;
  protected Vector3f direction;
  protected Vector2f result;
}
