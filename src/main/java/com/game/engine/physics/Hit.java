package com.game.engine.physics;

import com.game.engine.scene.entities.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
public class Hit {
  protected Ray ray;
  protected Entity entity;
  protected int meshId;

  public Hit() {
    ray = null;
    entity = null;
    meshId = -1;
  }
}
