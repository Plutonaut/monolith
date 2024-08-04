package com.game.engine.scene.entities.controllers;

import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EController;

import java.util.ArrayList;
import java.util.List;

public class EntityInstanceController extends AbstractEntityController {
  protected final ArrayList<ModelTransform> transforms;

  public EntityInstanceController() {
    transforms = new ArrayList<>();
  }

  @Override
  public String type() { return EController.INSTANCE.value(); }

  @Override
  public void onUpdate(ModelTransform transform) { }

  public void set(int instances) {
    for (int i = 0; i < instances; i++) add(new ModelTransform());
  }

  public ModelTransform get(int instance) { return transforms.get(instance); }

  public void remove(int instance) { transforms.remove(instance); }

  public void add(List<ModelTransform> transforms) {
    transforms.forEach(this::add);
  }

  public void add(ModelTransform transform) {
    this.transforms.add(transform);
  }
}
