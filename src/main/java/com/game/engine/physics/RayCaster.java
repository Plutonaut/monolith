package com.game.engine.physics;

import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;

import com.game.engine.window.Window;
import com.game.utils.application.LambdaValue;
import com.game.utils.math.VectorUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashSet;
import java.util.stream.Stream;

public class RayCaster {
  private final Vector2f result;
  private final LambdaValue closestDistance;
  private final HashSet<IHitListener> listeners;

  public RayCaster() {
    result = new Vector2f();
    closestDistance = new LambdaValue(Float.POSITIVE_INFINITY);
    listeners = new HashSet<>();
  }

  public void addListener(IHitListener listener) { listeners.add(listener); }

  void emit(Hit hit) { listeners.forEach((l) -> l.onHit(hit));}

  public void rayCastMouseClick(
    IHitListener listener,
    boolean all,
    Stream<Entity> entityCollection,
    Window window,
    Camera camera,
    Matrix4f projection
  ) {
    addListener(listener);
    result.set(0, 0);
    closestDistance.reset();

    Vector2f mousePosition = window.mouse().position();
    Vector3f normalizedDeviceSpace = window.normalizedDeviceSpace(mousePosition);
    Hit hit = new Hit();

    entityCollection.forEach(entity -> {
      if (!entity.controllers().hasPhysics()) return;
      Vector4f viewSpace = VectorUtils.viewSpace(projection, normalizedDeviceSpace);
      Vector4f worldSpace = camera.worldPosition(viewSpace);
      Vector3f dir = new Vector3f(worldSpace.x, worldSpace.y, worldSpace.z);
      Ray ray = new Ray(camera.position(), dir, result);
      Mesh mesh = entity.intersects(ray);
      if (mesh != null) {
        hit.ray(ray);
        if (all) {
          hit.entity(entity);
          hit.meshId(mesh.glId());
          listener.onHit(hit);
        } else if (ray.result().x < closestDistance.value()) {
          hit.entity(entity);
          hit.meshId(mesh.glId());
          closestDistance.set(ray.result().x);
        }
      }
    });
    if (!all) emit(hit);
  }
}
