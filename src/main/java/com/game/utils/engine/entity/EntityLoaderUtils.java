package com.game.utils.engine.entity;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.animations.AnimInfo;
import com.game.engine.render.mesh.animations.Bone;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.AnimationController;
import com.game.graphics.materials.Material;
import com.game.utils.application.LoaderUtils;
import com.game.utils.engine.MaterialUtils;
import com.game.utils.engine.MeshInfoUtils;
import com.game.utils.enums.EAttribute;
import com.game.utils.enums.EModifier;
import com.game.utils.enums.ERenderer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class EntityLoaderUtils {
  public static final int BASE_FLAGS = aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace | aiProcess_LimitBoneWeights;

  public Entity load(String id, String path, boolean animated, ERenderer shader) {
    Entity entity = new Entity(id);
    entity.addShaders(shader);

    int flags = BASE_FLAGS;
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
        Material material = materialIndex >= 0 && materialIndex < materialCount ? materials.get(i) : new Material();
        Mesh mesh = processMesh(aiMesh, material, bones);
        entity.addMesh(mesh);
      }

      if (animated) {
        AnimationController animationController = AnimationUtils.process(scene, bones);
        entity.addController(animationController);
        entity.toggleModifier(EModifier.ANIMATED);
      }
    }

    Assimp.aiReleaseImport(scene);

    return entity;
  }

  Mesh processMesh(AIMesh aiMesh, Material material, List<Bone> bones) {
    String meshId = aiMesh.mName().dataString();
    MeshInfo meshInfo = GlobalCache.instance().meshInfo(meshId);
    if (meshInfo == null) {
      meshInfo = MeshInfoUtils.process(aiMesh);
      AnimInfo animInfo = AnimationInfoUtils.process(aiMesh, bones);

      if (animInfo != null) meshInfo.addVertices(animInfo.boneIds(), GL46.GL_INT, 4, EAttribute.BON.getValue(), 1)
                                    .addVertices(animInfo.weights(), GL46.GL_FLOAT, 4, EAttribute.WGT.getValue(), 1);
      GlobalCache.instance().meshInfo(meshInfo);
    }

    // frog attach
    Mesh mesh = new Mesh(meshId);
    mesh.material(material);
    return mesh;
  }
}
