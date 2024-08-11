package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.animations.AnimInfo;
import com.game.engine.render.mesh.animations.Bone;
import com.game.engine.render.models.Model;
import com.game.graphics.materials.Material;
import com.game.utils.application.PathSanitizer;
import com.game.utils.application.values.ValueMap;
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

public class ObjectResourceModelGenerator extends AbstractModelGenerator {
  public static final int BASE_FLAGS = aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace | aiProcess_LimitBoneWeights;

  MeshInfo generateMeshInfo(AIMesh aiMesh, List<Bone> bones) {
    String meshId = aiMesh.mName().dataString();
    return GlobalCache.instance().meshInfo(meshId, name -> {
      MeshInfo meshInfo = MeshInfoUtils.process(aiMesh);
      AnimInfo animInfo = AnimationInfoUtils.process(aiMesh, bones);

      if (animInfo != null)
        meshInfo
          .addVertices(
            animInfo.boneIds(),
            GL46.GL_INT,
            GL46.GL_STATIC_DRAW,
            4,
            EAttribute.BON.getValue(),
            1,
            0
          )
          .addVertices(
            animInfo.weights(),
            GL46.GL_FLOAT,
            GL46.GL_STATIC_DRAW,
            4,
            EAttribute.WGT.getValue(),
            1,
            0
          );
      return meshInfo;
    });
  }

  @Override
  public Model generateModel(ValueMap map) {
    String name = map.get("id");
    String path = map.get("path");
    boolean animated = map.getBool("animated");

    Model model = new Model(name);
    int flags = BASE_FLAGS;
    if (animated) flags |= aiProcess_PreTransformVertices;

    String sanitizedPath = PathSanitizer.sanitizeFilePath(path);

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
        MeshInfo meshInfo = generateMeshInfo(aiMesh, bones);
        Material material = materialIndex >= 0 && materialIndex < materialCount
                            ? materials.get(materialIndex)
                            : null;
        if (material != null) {
          meshInfo.material(material);
        }
        model.addMeshData(meshInfo.name());
      }

      if (animated)
        model.addAnimations(AnimationUtils.process(scene, bones));
    }

    Assimp.aiReleaseImport(scene);

    return model;
  }
}
