package com.game.engine.render.pipeline.load;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.animations.AnimInfo;
import com.game.engine.render.mesh.animations.Bone;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.AbstractRenderPipe;
import com.game.engine.render.pipeline.IRenderPacket;
import com.game.engine.render.pipeline.ModelPacket;
import com.game.graphics.materials.Material;
import com.game.utils.application.LoaderUtils;
import com.game.utils.engine.MaterialUtils;
import com.game.utils.engine.MeshInfoUtils;
import com.game.utils.engine.entity.AnimationInfoUtils;
import com.game.utils.engine.entity.AnimationUtils;
import com.game.utils.enums.EAttribute;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class LoadResourcePipe extends AbstractRenderPipe {

  @Override
  public IRenderPacket onPoll(IRenderPacket packet) {
    typeCheck(packet, LoadResourcePacket.class);
    LoadResourcePacket loadResourcePacket = (LoadResourcePacket) packet;
    Model model = load(loadResourcePacket.path(), loadResourcePacket.animated());
    return new ModelPacket(loadResourcePacket.name(), model, packet.destination());
  }

  Model load(String path, boolean animated) {
    Model model = new Model(path);
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
        }
      }

      if (animated)
        model.addAnimations(AnimationUtils.process(scene, bones));
    }

    Assimp.aiReleaseImport(scene);

    return model;
  }

  MeshInfo processMesh(AIMesh aiMesh, List<Bone> bones) {
    String meshId = aiMesh.mName().dataString();
    return GlobalCache.instance().meshInfo(meshId, name -> {
      MeshInfo meshInfo = MeshInfoUtils.process(aiMesh);
      AnimInfo animInfo = AnimationInfoUtils.process(aiMesh, bones);

      if (animInfo != null)
        meshInfo.addVertices(animInfo.boneIds(), GL46.GL_INT, 4, EAttribute.BON.getValue(), 1)
                .addVertices(animInfo.weights(), GL46.GL_FLOAT, 4, EAttribute.WGT.getValue(), 1);
      GlobalCache.instance().cacheItem(meshInfo);

      return meshInfo;
    });
  }
}