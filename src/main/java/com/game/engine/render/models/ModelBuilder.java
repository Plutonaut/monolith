package com.game.engine.render.models;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.animations.AnimInfo;
import com.game.engine.render.mesh.animations.Bone;
import com.game.engine.render.mesh.definitions.MeshDefinition;
import com.game.engine.scene.entities.animations.Animation;
import com.game.engine.scene.entities.animations.Frame;
import com.game.graphics.materials.Material;
import com.game.utils.application.LoaderUtils;
import com.game.utils.engine.MaterialUtils;
import com.game.utils.engine.MeshInfoUtils;
import com.game.utils.engine.entity.AnimationInfoUtils;
import com.game.utils.engine.entity.AnimationUtils;
import com.game.utils.enums.EAttribute;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

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

  public ModelBuilder addAnimation(String name, double duration, List<Frame> frames) {
    return addAnimation(new Animation(name, duration, frames));
  }

  public ModelBuilder addAnimation(Animation animation) {
    if (animation != null) animations.add(animation);
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

  public ModelBuilder load(boolean animated) {
    if (path == null) {
      return null;
    }

    int flags = LoaderUtils.BASE_FLAGS;
    if (animated) flags |= aiProcess_PreTransformVertices;

    String sanitizedPath = LoaderUtils.sanitizeFilePath(path);

    AIScene scene = Assimp.aiImportFile(sanitizedPath, flags);
    if (scene == null)
      throw new RuntimeException("Could not load model from path: " + sanitizedPath + System.lineSeparator() + Assimp.aiGetErrorString());

    PointerBuffer aiMeshes = scene.mMeshes();
    if (aiMeshes != null) {
      List<Material> materials = MaterialUtils.process(sanitizedPath, scene);
      List<Bone> bones = new ArrayList<>();
      int materialCount = materials.size();
      int meshCount = scene.mNumMeshes();
      for (int i = 0; i < meshCount; i++) {
        AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
        int materialIndex = aiMesh.mMaterialIndex();
        MeshInfo meshInfo = processMesh(aiMesh, bones);
        Material material = materialIndex >= 0 && materialIndex < materialCount
                            ? materials.get(i)
                            : null;
        if (material != null) {
          meshInfo.material(material.name());
          addMeshData(meshInfo);
        }
      }

      if (animated) {
        List<Animation> animations = AnimationUtils.process(scene, bones);
        animations.forEach(this::addAnimation);
      }
    }

    Assimp.aiReleaseImport(scene);
    return this;
  }

  MeshInfo processMesh(AIMesh aiMesh, List<Bone> bones) {
    String meshId = aiMesh.mName().dataString();
    return GlobalCache.instance().meshInfo(meshId, name -> {
      MeshInfo meshInfo = MeshInfoUtils.process(aiMesh);
      AnimInfo animInfo = AnimationInfoUtils.process(aiMesh, bones);

      if (animInfo != null) meshInfo
        .addVertices(animInfo.boneIds(), GL46.GL_INT, 4, EAttribute.BON.getValue(), 1)
        .addVertices(animInfo.weights(), GL46.GL_FLOAT, 4, EAttribute.WGT.getValue(), 1);
      GlobalCache.instance().cacheItem(meshInfo);

      return meshInfo;
    });
  }

  protected void dispose() {
    path = null;
    animations.clear();
    meshData.clear();
  }
}
