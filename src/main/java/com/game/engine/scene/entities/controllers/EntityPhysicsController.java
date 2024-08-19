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

  public Mesh intersects(List<Mesh> meshes, Ray ray) {
    return meshes.stream().filter(mesh -> intersects(mesh, ray)).findFirst().orElse(null);
  }

  boolean intersects(Mesh mesh, Ray ray) {
    Vector3f min = vertex(mesh.bounds().minVertex());
    Vector3f max = vertex(mesh.bounds().maxVertex());

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
