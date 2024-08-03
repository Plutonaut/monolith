package com.game.engine.render.models;

import com.game.caches.GlobalCache;
import com.game.caches.models.interfaces.IModelCachable;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.animations.Animation;
import com.game.utils.enums.EModelCache;
import com.game.utils.enums.EModifier;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Model implements IModelCachable {
  private final String name;
  private final List<String> meshData;
  private final List<Animation> animations;

  public Model(String name) {
    this.name = name;

    animations = new ArrayList<>();
    meshData = new ArrayList<>();
  }

  public List<MeshInfo> meshInfo() {
    return meshData.stream().map(GlobalCache.instance()::meshInfo).toList();
  }

  public Entity create(String entityName) {
    Entity entity = new Entity(entityName);
    if (!animations().isEmpty())
      entity.addAnimations(animations).toggleModifier(EModifier.ANIMATED);

    return entity;
  }

  public void addMeshData(String meshInfo) { this.meshData.add(meshInfo); }

  public void addAnimations(List<? extends Animation> animations) {
    this.animations.addAll(animations);
  }

  @Override
  public EModelCache type() { return EModelCache.MODEL; }
}
