package com.game.engine.scene.entities.controllers;

import com.game.engine.scene.entities.Entity;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public abstract class AbstractEntityController {
  protected String entityId;

  public abstract String type();

  public abstract void onUpdate();

  public AbstractEntityController onAttach(Entity entity) {
    this.entityId = entity.name();

    return this;
  }
}
