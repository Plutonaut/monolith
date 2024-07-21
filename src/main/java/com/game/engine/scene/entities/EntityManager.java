package com.game.engine.scene.entities;

import com.game.caches.EntityNameResolver;

import java.util.HashMap;

public class EntityManager {
  private final HashMap<String, Entity> entities;
  private final EntityNameResolver entityNameResolver;

  public EntityManager() {
    entities = new HashMap<>();
    entityNameResolver = new EntityNameResolver();
  }

  public Entity get(String id) {
    return entities.get(id);
  }

  public String getAvailableEntityName(String entityName) {
    return entityNameResolver.getAvailable(entityName);
  }

  public void add(Entity entity) {
    entities.put(entity.name(), entity);
  }
}
