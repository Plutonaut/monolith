package com.game.engine.scene.entities.controllers;

import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EController;

import java.util.HashMap;

public class EntityControllerManager {
  protected final HashMap<String, AbstractEntityController> controllers;

  public EntityControllerManager() {
    controllers = new HashMap<>();
  }

  public void onUpdate(ModelTransform transform) {
    controllers.values().forEach(controller -> controller.onUpdate(transform));
  }

  public interface IControllerGenerator {
    AbstractEntityController generate(String type);
  }

  public EntityAudioController audio() {
    return (EntityAudioController) controller(EController.AUDIO.getValue(), k -> new EntityAudioController());
  }

  public EntityAnimationController animations() {
    return (EntityAnimationController) controller(EController.ANIM.getValue(), k -> new EntityAnimationController());
  }

  public EntityPhysicsController physics() {
    return (EntityPhysicsController) controller(EController.PHYS.getValue(), k -> new EntityPhysicsController());
  }

  public boolean hasAnimations() {
    return hasController(EController.ANIM.getValue());
  }

  public boolean hasPhysics() {
    return hasController(EController.PHYS.getValue());
  }

  public AbstractEntityController controller(String type, IControllerGenerator generator) {
    return controllers.computeIfAbsent(type, generator::generate);
  }

  boolean hasController(String type) {
    return controllers.containsKey(type);
  }
}
