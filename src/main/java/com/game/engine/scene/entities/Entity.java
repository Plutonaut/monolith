package com.game.engine.scene.entities;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.entities.controllers.AbstractEntityController;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EModifier;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Entity implements IRenderable {
  protected final HashMap<String, AbstractEntityController> controllers;
  protected final ArrayList<EModifier> modifiers;
  protected final ModelTransform transform;
  protected final ArrayList<Mesh> meshes;
  protected final String name;
  protected final int id;

  public Entity(String name) {
    this(name, new ArrayList<>());
  }

  public Entity(String name, ArrayList<Mesh> meshes) {
    this.name = name;
    this.id = System.identityHashCode(name);
    this.meshes = meshes;

    transform = new ModelTransform();
    modifiers = new ArrayList<>();
    controllers = new HashMap<>();
  }

  public Entity move(Vector3f position) {
    transform.position().set(position);
    return this;
  }

  public Entity move(float x, float y) {
    return move(x, y, 0f);
  }

  public Entity move(float x, float y, float z) {
    transform.position().set(x, y, z);
    return this;
  }

  public Entity moveToward(Vector3f position) {
    transform.position().add(position);
    return this;
  }

  public Entity moveToward(float x, float y, float z) {
    transform.position().add(x, y, z);
    return this;
  }

  public Entity scale(float scale) {
    transform.scale(scale);
    return this;
  }

  public void toggleModifier(EModifier modifier) {
    if (isModifierActive(modifier)) modifiers.remove(modifier);
    else modifiers.add(modifier);
  }

  public boolean isModifierActive(EModifier modifier) {
    return modifiers.contains(modifier);
  }

  public void addMeshes(List<Mesh> meshes) {
    this.meshes.addAll(meshes);
  }

  public void addMesh(Mesh mesh) {
    this.meshes.add(mesh);
  }

  public void addController(AbstractEntityController controller) {
    controllers.put(controller.type(), controller.onAttach(this));
  }

  public AbstractEntityController controller(String type) {
    return controllers.get(type);
  }
}
