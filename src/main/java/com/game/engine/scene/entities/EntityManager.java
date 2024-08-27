package com.game.engine.scene.entities;

import com.game.caches.EntityNameResolver;
import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.animations.Animation;
import com.game.graphics.shaders.Program;
import com.game.utils.enums.EGLParam;
import com.game.utils.enums.EModifier;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true)
@Slf4j
public class EntityManager {
  @Getter
  private final HashMap<String, Entity> entities;
  private final EntityNameResolver entityNameResolver;

  public EntityManager() {
    entities = new HashMap<>();
    entityNameResolver = new EntityNameResolver();
  }

  String getAvailableEntityName(String entityName) {
    return entityNameResolver.getAvailable(entityName);
  }

  Entity add(Entity entity) {
    String entityName = getAvailableEntityName(entity.name());
    entities.put(entityName, entity);
    return entity;
  }

  public Entity get(String id) {
    return entities.get(id);
  }

  public Entity create(
    String entityName, List<Mesh> meshes,
    ERenderer shader,
    EProjection projection,
    EGLParam... params
  ) {
    EntityRenderParameters parameters = new EntityRenderParameters(
      shader,
      projection,
      EGLParam.bitmap(params)
    );
    Entity entity = new Entity(entityName, parameters);
    entity.addMeshes(meshes);
    return add(entity);
  }

  public Entity create(
    String entityName,
    Model model,
    ERenderer shader,
    EProjection projection,
    EGLParam... params
  ) {
    EntityRenderParameters parameters = new EntityRenderParameters(
      shader,
      projection,
      EGLParam.bitmap(params)
    );
    Entity entity = new Entity(entityName, parameters);
    Program program = GlobalCache.instance().program(shader.key());
    List<Animation> animations = model.animations();
    if (!animations.isEmpty()) entity.addAnimations(animations).parameters().toggleModifier(
      EModifier.ANIMATED);
    model.meshInfo().forEach(info -> {
      Mesh mesh = GlobalCache.instance().mesh(info.name(), (name) -> info.create(program));
      entity.addMesh(mesh);
      if (info instanceof FontMeshInfo fontMeshInfo) entity.controllers().text().mesh(mesh).text(
        fontMeshInfo.text()).font(fontMeshInfo.font());
    });
    return add(entity);
  }
}
