package com.game.engine.physics;

import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;
import com.game.engine.window.Window;
import com.game.utils.math.VectorUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Collection;

public class Raycaster {
  public static void rayCastMouseClick(
    IHitListener listener,
    boolean all,
    Collection<Entity> entityCollection,
    Window window,
    Camera camera,
    Matrix4f projection
  ) {
    Vector2f mousePosition = window.mouse().position();
    Vector3f normalizedDeviceSpace = window.normalizedDeviceSpace(mousePosition);
    Hit hit = new Hit();
    float closestDistance = Float.POSITIVE_INFINITY;
    Vector2f result = new Vector2f();
    for (Entity entity : entityCollection) {
      if (!entity.controllers().hasPhysics()) continue;
      Vector4f viewSpace = VectorUtils.viewSpace(projection, normalizedDeviceSpace);
      Vector4f worldSpace = camera.worldPosition(viewSpace);
      Vector3f dir = new Vector3f(worldSpace.x, worldSpace.y, worldSpace.z);
      Ray ray = new Ray(camera.position(), dir, result);
      boolean intersects = entity.intersects(ray);
      if (intersects) {
        hit.ray(ray);
        if (all) {
          hit.entity(entity);
          listener.onHit(hit);
        } else if (ray.result().x < closestDistance) {
          hit.entity(entity);
          closestDistance = ray.result().x;
        }
      }
    }
    if (!all) listener.onHit(hit);
  }
}
