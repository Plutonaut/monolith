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

  public void copy(EntityControllerManager to) {
    if (to == this) return;
    controllers.forEach(to::controller);
  }

  public EntityTextController text() {
    return (EntityTextController) controller(
      EController.TEXT.value(),
      k -> new EntityTextController()
    );
  }

  public EntityAudioController audio() {
    return (EntityAudioController) controller(
      EController.AUDIO.value(),
      k -> new EntityAudioController()
    );
  }

  public EntityAnimationController animations() {
    return (EntityAnimationController) controller(
      EController.ANIM.value(),
      k -> new EntityAnimationController()
    );
  }

  public EntityPhysicsController physics() {
    return (EntityPhysicsController) controller(
      EController.PHYS.value(),
      k -> new EntityPhysicsController()
    );
  }

  public EntityInstanceController instances() {
    return (EntityInstanceController) controller(
      EController.INSTANCE.value(),
      k -> new EntityInstanceController()
    );
  }

  public boolean hasAnimations() {
    return hasController(EController.ANIM.value());
  }

  public boolean hasPhysics() {
    return hasController(EController.PHYS.value());
  }

  public AbstractEntityController controller(String type, AbstractEntityController controller) {
    return controllers.put(type, controller);
  }

  public AbstractEntityController controller(String type, IControllerGenerator generator) {
    return controllers.computeIfAbsent(type, generator::generate);
  }

  boolean hasController(String type) {
    return controllers.containsKey(type);
  }

  public interface IControllerGenerator {
    AbstractEntityController generate(String type);
  }
}
