package com.game.engine.scene.entities;

import com.game.caches.EntityNameResolver;
import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.animations.Animation;
import com.game.graphics.shaders.Program;
import com.game.utils.enums.EModifier;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidParameterException;
import java.util.Collection;
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

  Mesh createMesh(MeshInfo info, Program program, IMeshGenerator generator) {
    GlobalCache instance = GlobalCache.instance();
    return generator == null
           ? instance.mesh(info.name())
           : instance.mesh(info.name(), (name) -> generator.generate(info, program));
  }

  public Entity add(Entity entity) {
    entities.put(entity.name(), entity);
    return entity;
  }

  public Entity get(String id) {
    return entities.get(id);
  }

  public Collection<Entity> getAll() {
    return entities.values();
  }

  public Entity createFrom(String entityName) {
    if (!entities.containsKey(entityName)) throw new InvalidParameterException(
      "Cannot create new instance of entity %s as it does not exist!".formatted(entityName));

    Entity from = get(entityName);
    String name = getAvailableEntityName(entityName);
    Entity to = new Entity(name, from.meshes(), from.parameters);
    from.controllers().copy(to.controllers());
    return add(to);
  }

  public Entity create(
    String id, Model model, EntityRenderParameters parameters, boolean fromCache
  ) {
    String entityName = getAvailableEntityName(id);
    Program program = GlobalCache.instance().program(parameters.shader.key());
    Entity entity = new Entity(entityName, parameters);
    List<Animation> animations = model.animations();
    if (!animations.isEmpty())
      entity.addAnimations(animations).parameters().toggleModifier(EModifier.ANIMATED);
    model.meshInfo().forEach(info -> {
      if (info instanceof FontMeshInfo fontMeshInfo) {
        DynamicMesh mesh = (DynamicMesh) createMesh(
          info,
          program,
          fromCache ? null : this::createDynamicMesh
        );
        entity.controllers().text().mesh(mesh).text(fontMeshInfo.text()).font(fontMeshInfo.font());
      } else {
        Mesh mesh = createMesh(info, program, fromCache ? null : this::createMesh);
//        if (info.instances() > 1) entity.controllers().instances().set(info.instances());
        entity.addMesh(mesh);
      }
    });

    log.info("Successfully created entity {}", entityName);
    return add(entity);
  }

  Mesh createMesh(MeshInfo info, Program program) {
    Mesh mesh = info.create();
    mesh.bind();
    info.vertices().forEach(vertex -> {
      mesh.addVertexBufferObject(vertex.create());
      mesh.setVertexAttributeArray(program.attributes().point(vertex));
    });
    if (mesh.isComplex()) {
      IndexBufferObject ibo = new IndexBufferObject();
      mesh.vbos().add(ibo);
      ibo.buffer(info.indices().asIntArray());
    }
    mesh.unbind();
    return mesh;
  }

  DynamicMesh createDynamicMesh(MeshInfo info, Program program) {
    FontMeshInfo fontMeshInfo = (FontMeshInfo) info;
    DynamicMesh mesh = fontMeshInfo.create();
    mesh.redraw(info, v -> mesh.setVertexAttributeArray(program.attributes().point(v)));
    return mesh;
  }

  interface IMeshGenerator {
    Mesh generate(MeshInfo info, Program program);
  }
}
