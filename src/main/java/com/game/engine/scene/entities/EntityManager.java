package com.game.engine.scene.entities;

import java.util.HashMap;
import java.util.stream.Stream;

public class EntityManager {
  private final HashMap<String, Entity> entities;

  public EntityManager() {
    entities = new HashMap<>();
  }

  public Entity get(String id) {
    return entities.get(id);
  }

  public void add(Entity entity) {
    entities.put(entity.name(), entity);
  }
}
