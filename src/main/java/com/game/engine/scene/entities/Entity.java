package com.game.engine.scene.entities;

import com.game.engine.compute.Instruction;
import com.game.engine.physics.Ray;
import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.audio.AudioSource;
import com.game.engine.scene.entities.animations.Animation;
import com.game.engine.scene.entities.controllers.AbstractEntityController;
import com.game.engine.scene.entities.controllers.EntityControllerManager;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EModifier;
import com.game.utils.enums.EProjection;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Quaternionf;
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
  protected final ArrayList<Instruction> instructions;
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

    instructions = new ArrayList<>();
    transform = new ModelTransform();
    controllers = new EntityControllerManager(this::controllerMeshCallback);

    addMeshes(meshes);
  }

  @Override
  public int glParamFlags() {
    return parameters.glParamFlags;
  }

  public Instruction instruction(String name) {
    return instructions.stream().filter(i -> i.name().equals(name)).findFirst().orElse(null);
  }

  public Mesh mesh() {
    return meshes.getFirst();
  }

  public Mesh meshAtIndex(int index) {
    return index < meshes.size() ? meshes.get(index) : null;
  }

  public Mesh meshByGlID(int glId) {
    return meshes.stream().filter(m -> m.glId() == glId).findFirst().orElse(null);
  }

  public Mesh controllerMeshCallback(int glId, AbstractEntityController.IMeshGenerator generator) {
    Mesh mesh = glId == -1 ? null : meshByGlID(glId);
    if (mesh == null && generator != null) {
      mesh = generator.generate();
      if (mesh != null) meshes.add(mesh);
    }
    return mesh;
  }

  public void onMeshSelected(int meshId) {
    meshes().forEach(m -> m.selected(m.glId() == meshId));
  }

  public EProjection projection() {
    return parameters.projection;
  }

  public Entity move(Vector3f position) {
    return move(position.x(), position.y(), position.z());
  }

  public Entity move(float p) {
    return move(p, p, p);
  }

  public Entity move2D(float x, float y) {
    return move(x, y, 0f);
  }

  public Entity move(float x, float y, float z) {
    transform.position().set(x, y, z);
    return onTransformUpdate();
  }

  public Entity rotate(Vector3f axis, float angle) {
    return rotate(axis.x, axis.y, axis.z, angle);
  }

  public Entity rotate(float x, float y, float z, float angle) {
    transform.rotation().set(new Quaternionf(x, y, z, angle));
    return onTransformUpdate();
  }

  public Entity rotateX(float angle) {
    transform.rotation().rotateX(angle);
    return onTransformUpdate();
  }

  public Entity rotateY(float angle) {
    transform.rotation().rotateY(angle);
    return onTransformUpdate();
  }

  public Entity rotateZ(float angle) {
    transform.rotation().rotateZ(angle);
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

  public Entity addAnimations(List<Animation> animations) {
    controllers.animations().add(animations);
    return onTransformUpdate();
  }

  public Entity addAudio(AudioSource source) {
    controllers.audio().add(source);
    return onTransformUpdate();
  }

  public Entity addPhysics() {
    controllers.physics();
    return onTransformUpdate();
  }

  public Entity redrawText(String text) {
    controllers().text().redraw(text);
    return onTransformUpdate();
  }

  public boolean isModifierActive(EModifier modifier) {
    return parameters().isModifierActive(modifier);
  }

  public Mesh intersects(Ray ray) {
    return controllers.hasPhysics() ? onTransformUpdate().controllers().physics().intersects(meshes,
                                                                                             ray
    ) : null;
  }
}
