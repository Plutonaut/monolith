package com.game.utils.math;

import com.game.engine.physics.Bounds2D;
import com.game.utils.application.values.ValueStore;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VertexUtils {
  public static ValueStore scalePositionVertices2D(Bounds2D bounds) {
    Vector2f min = bounds.min();
    Vector2f max = bounds.max();
    Vector3f tl = new Vector3f(min.x(), max.y(), 0f);
    Vector3f bl = new Vector3f(min.x(), min.y(), 0f);
    Vector3f br = new Vector3f(max.x(), min.y(), 0f);
    Vector3f tr = new Vector3f(max.x(), max.y(), 0f);

    ValueStore positionStore = new ValueStore();

    positionStore.add(tl);
    positionStore.add(bl);
    positionStore.add(br);
    positionStore.add(tr);

    return positionStore;
  }
}
