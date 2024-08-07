package com.game.engine.scene.entities;

import com.game.engine.physics.Ray;
import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.audio.AudioSource;
import com.game.engine.scene.entities.animations.Animation;
import com.game.engine.scene.entities.controllers.EntityControllerManager;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EModifier;
import com.game.utils.enums.EProjection;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Entity implements IRenderable {
  protected final EntityControllerManager controllers;
  protected final EntityRenderParameters parameters;
  protected final ModelTransform transform;
  // TODO: Convert to hashmap, perform check for duplicates prior to adding mesh
  protected final ArrayList<Mesh> meshes;
  protected final String name;
  protected final int id;

  public Entity(String name) {
    this(name, new ArrayList<>());
  }

  public Entity(String name, ArrayList<Mesh> meshes) {
    this(name, meshes, new EntityRenderParameters());
  }

  public Entity(String name, EntityRenderParameters parameters) {
    this(name, new ArrayList<>(), parameters);
  }

  public Entity(String name, ArrayList<Mesh> meshes, EntityRenderParameters parameters) {
    this.name = name;
    this.id = System.identityHashCode(name);
    this.meshes = new ArrayList<>();
    this.parameters = parameters;

    transform = new ModelTransform();
    controllers = new EntityControllerManager();

    addMeshes(meshes);
  }

  @Override
  public int glParamFlags() {
    return parameters.glParamFlags;
  }

  public EProjection projection() {
    return parameters.projection;
  }

  public Entity move(Vector3f position) {
    return move(position.x(), position.y(), position.z());
  }

  public Entity move(float x, float y) {
    return move(x, y, 0f);
  }

  public Entity move(float x, float y, float z) {
    transform.position().set(x, y, z);
    return onTransformUpdate();
  }

  public Entity scale(float scale) {
    transform.scale(scale);
    return onTransformUpdate();
  }

  Entity onTransformUpdate() {
    controllers.onUpdate(transform);
    return this;
  }

  public void addMeshes(List<Mesh> meshes) {
    meshes.forEach(this::addMesh);
  }

  public void addMesh(Mesh mesh) {
    meshes.add(mesh);
  }

  public Entity addPhysics() {
    controllers.physics();
    return onTransformUpdate();
  }

  public Entity addAnimations(List<Animation> animations) {
    controllers.animations().add(animations);
    return onTransformUpdate();
  }

  public Entity addAudio(AudioSource source) {
    controllers.audio().add(source);
    return onTransformUpdate();
  }

  public boolean isModifierActive(EModifier modifier) {
    return parameters().isModifierActive(modifier);
  }

  public void redrawText(String text) {
    controllers().text().redraw(text);
    onTransformUpdate();
  }

  public boolean intersects(Ray ray) {
    return controllers.hasPhysics() && onTransformUpdate()
      .controllers()
      .physics()
      .intersects(meshes, ray);
  }
}
