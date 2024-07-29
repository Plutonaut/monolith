package com.game.engine.physics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
public class Bounds3D {
  protected Vector3f min;
  protected Vector3f max;
}
