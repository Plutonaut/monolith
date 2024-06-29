package com.game.engine.scene.entities;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.entities.controllers.AbstractController;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EModifier;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class Entity implements IRenderable {
  protected final HashMap<String, AbstractController> controllers;
  protected final ArrayList<Mesh> meshes;
  protected final ArrayList<ERenderer> shaders;
  protected final ArrayList<EModifier> modifiers;
  protected final String name;
  protected final int id;
  protected final ModelTransform transform;

  public Entity(String name) {
    this.name = name;
    this.id = System.identityHashCode(name);

    modifiers = new ArrayList<>();
    shaders = new ArrayList<>();
    controllers = new HashMap<>();
    meshes = new ArrayList<>();
    transform = new ModelTransform();
  }

  public Vector3f position() { return transform.position(); }

  public Entity move(float x, float y, float z) {
    transform.position().set(x, y, z);
    return this;
  }

  public Vector3f rotation() { return transform.rotation(); }

  public Entity rotate(float x, float y, float z) {
    transform.rotation().set(x, y, z);
    return this;
  }

  public Entity rotate(float r) {
    transform.rotation().set(r);
    return this;
  }

  public float scale() { return transform.scale(); }

  public Entity scale(float s) {
    transform.scale(s);
    return this;
  }

  public void setTransform(ModelTransform transform) {
    this.transform.set(transform);
  }

  // frog this won't work... creating new VBOS for every shader we attach...
  public void addMesh(Mesh mesh) {
    shaders.forEach(mesh::attach);
    meshes.add(mesh);
  }

  public void addShaders(ERenderer... shaders) {
    this.shaders.addAll(Arrays.asList(shaders));
  }

  public void toggleModifier(EModifier modifier) {
    if (isModifierActive(modifier)) modifiers.remove(modifier);
    else modifiers.add(modifier);
  }

  public boolean isModifierActive(EModifier modifier) {
    return modifiers.contains(modifier);
  }

  public void addController(AbstractController controller) {
    controllers.put(controller.type(), controller.onAttach(this));
  }

  public AbstractController controller(String type) {
    return controllers.get(type);
  }
}
