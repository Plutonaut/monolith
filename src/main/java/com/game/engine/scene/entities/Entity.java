package com.game.engine.scene.entities;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.entities.controllers.AbstractController;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EModifier;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Entity implements IRenderable {
  protected final HashMap<String, AbstractController> controllers;
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

  public void addController(AbstractController controller) {
    controllers.put(controller.type(), controller.onAttach(this));
  }

  public AbstractController controller(String type) {
    return controllers.get(type);
  }
}
