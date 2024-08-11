package com.game.engine.render.models;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.scene.entities.animations.Animation;
import com.game.utils.enums.ECache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Model implements IModel {
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

  public void addMeshData(String meshInfo) { this.meshData.add(meshInfo); }

  public void addAnimations(List<? extends Animation> animations) {
    this.animations.addAll(animations);
  }

  @Override
  public ECache type() { return ECache.MODEL; }
}
