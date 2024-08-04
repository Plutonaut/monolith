package com.game.engine.scene.entities.controllers;

import com.game.engine.physics.Ray;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EController;
import org.joml.Intersectionf;
import org.joml.Vector3f;

import java.util.List;

public class EntityPhysicsController extends AbstractEntityController {
  protected Vector3f origin;
  protected float scale;

  public EntityPhysicsController() {
    origin = new Vector3f();
    scale = 1;
  }

  @Override
  public String type() {
    return EController.PHYS.value();
  }

  @Override
  public void onUpdate(ModelTransform transform) {
    origin = transform.position();
    scale = transform.scale();
  }

  public boolean intersects(List<Mesh> meshes, Ray ray) {
    return meshes.stream().anyMatch(mesh -> intersects(mesh, ray));
  }

  boolean intersects(Mesh mesh, Ray ray) {
    Vector3f min = vertex(mesh.min());
    Vector3f max = vertex(mesh.max());

    return Intersectionf.intersectRayAab(
      ray.origin(),
      ray.direction(),
      min,
      max,
      ray.result()
    );
  }

  Vector3f vertex(Vector3f value) {
    return new Vector3f(value).add(origin).mul(scale);
  }
}
