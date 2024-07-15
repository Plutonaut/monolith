package com.game.engine.render.models;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.definitions.MeshDefinition;
import com.game.engine.scene.entities.animations.Animation;
import com.game.engine.scene.entities.animations.Animation3D;
import com.game.engine.scene.entities.animations.BoneMatrixFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModelBuilder {
  protected String path;
  protected ArrayList<Animation> animations;
  protected ArrayList<String> meshData;

  public ModelBuilder() {
    path = null;
    animations = new ArrayList<>();
    meshData = new ArrayList<>();
  }

  public ModelBuilder use(String name) {
    if (this.path != null) {
      log.error("Pipeline currently in use! Please call flush before attempting to use");
      return null;
    }
    this.path = name;
    return this;
  }

  public ModelBuilder addAnimation(String name, double duration, List<BoneMatrixFrame> boneMatrixFrames) {
    return addAnimation(new Animation3D(name, duration, boneMatrixFrames));
  }

  public ModelBuilder addAnimation(Animation animation3D) {
    if (animation3D != null) animations.add(animation3D);
    return this;
  }

  public ModelBuilder addMeshData(MeshDefinition meshDefinition) {
    MeshInfo info = GlobalCache
      .instance()
      .meshInfo(meshDefinition.name(), n -> meshDefinition.createMeshInfo());
    return addMeshData(info.name());
  }

  public ModelBuilder addMeshData(MeshInfo meshInfo) {
    GlobalCache.instance().cacheItem(meshInfo);
    return addMeshData(meshInfo.name());
  }

  public ModelBuilder addMeshData(String meshInfoName) {
    if (meshInfoName != null) meshData.add(meshInfoName);
    return this;
  }

  public Model build() {
    Model model;
    if (path != null) {
      model = new Model(path);
      model.addAnimations(animations);
      meshData.forEach(model::addMeshData);
    } else model = null;
    dispose();
    return model;
  }

  protected void dispose() {
    path = null;
    animations.clear();
    meshData.clear();
  }
}
