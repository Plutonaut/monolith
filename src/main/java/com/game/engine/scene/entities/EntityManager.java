package com.game.engine.scene.entities;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.definitions.Quad;
import com.game.graphics.materials.Material;
import com.game.utils.engine.entity.EntityLoaderUtils;
import com.game.utils.enums.EMaterialTexture;
import com.game.utils.enums.ERenderer;

import java.util.HashMap;
import java.util.stream.Stream;

public class EntityManager {
  private final HashMap<String, Entity> entities;
  private final EntityLoaderUtils loader;

  public EntityManager() {
    entities = new HashMap<>();
    loader = new EntityLoaderUtils();
  }

  public Stream<Entity> renderStream() {
    return entities.values().stream();
  }

  public Entity get(String id) {
    return entities.get(id);
  }

  public void add(Entity entity) {
    entities.put(entity.name(), entity);
  }

  public Entity loadSkyBoxModel(String id, String path) {
    return load3DModelResource(id, path, false, ERenderer.SKYBOX);
  }

  public Entity load3DModel(String id, String path, boolean isAnimated) {
    return load3DModelResource(id, path, isAnimated, ERenderer.SCENE);
  }

  protected Entity load3DModelResource(String id, String path, boolean isAnimated, ERenderer shader) {
    return entities.computeIfAbsent(id, entityName -> loader.load(entityName, path, isAnimated, shader));
  }

  public Entity loadTestQuad(String id) {
    return entities.computeIfAbsent(id, entityName -> {
      Entity entity = new Entity(entityName);
      entity.addShaders(ERenderer.BASIC);
      Quad quad = new Quad();
      GlobalCache.instance().meshInfo(quad);
      Mesh mesh = new Mesh(quad.name());
      entity.addMesh(mesh);
      return entity;
    });
  }

  public Entity load2DSprite(String id, String path) {
    return entities.computeIfAbsent(id, entityName -> {
      Entity entity = new Entity(entityName);
      entity.addShaders(ERenderer.SPRITE);
      Quad quad = new Quad();
      GlobalCache.instance().meshInfo(quad);
      Material material = new Material();
      material.texture(EMaterialTexture.DIF.getValue(), path);
      Mesh mesh = new Mesh(quad.name());
      mesh.material(material);
      entity.addMesh(mesh);
      return entity;
    });
  }
}
