package com.game.utils.engine;

import com.game.engine.physics.Bounds2D;
import org.joml.Vector2f;

public class PhysicsUtils {
  public static boolean isPointInsideBounds2D(Vector2f point, Bounds2D bounds) {
    return isPointInsideBounds2D(point, bounds.minVertex(), bounds.maxVertex());
  }

  public static boolean isPointInsideBounds2D(Vector2f point, Vector2f min, Vector2f max) {
    return point.x() > min.x() && point.y() > min.y() && point.x() < max.x() && point.y() < max.y();
  }
}
