package com.game.utils.engine.entity;

import com.game.engine.render.mesh.animations.AnimInfo;
import com.game.engine.render.mesh.animations.Bone;
import com.game.engine.render.mesh.animations.Weight;
import com.game.utils.application.ValueStore;
import com.game.utils.math.MatrixUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVertexWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimationInfoUtils {
  public static final int MAX_WEIGHTS = 4;
  public static AnimInfo process(AIMesh aiMesh, final List<Bone> bones) {
    ValueStore boneIds = new ValueStore();
    ValueStore vertexWeights = new ValueStore();

    int count = aiMesh.mNumBones();
    PointerBuffer buffer = aiMesh.mBones();

    if (count == 0 || buffer == null) return null;

    HashMap<Integer, List<Weight>> weightMap = new HashMap<>();

    for (int i = 0; i < count; i++) {
      AIBone aiBone = AIBone.create(buffer.get(i));
      Bone bone = new Bone(i, aiBone.mName().dataString(), MatrixUtils.convert(aiBone.mOffsetMatrix()));
      bones.add(bone);

      List<Weight> weights = process(aiBone, i);
      weightMap.computeIfAbsent(i, id -> new ArrayList<>()).addAll(weights);
    }

    buffer.free();

    int vertexCount = aiMesh.mNumVertices();
    for (int i = 0; i < vertexCount; i++) {
      List<Weight> weights = weightMap.get(i);
      int size = weights.size();
      for (int j = 0; j < MAX_WEIGHTS; j++) {
        boolean s = j < size;
        vertexWeights.add(s ? weights.get(j).weight() : 0.0f);
        boneIds.add(s ? weights.get(j).boneId() : 0);
      }
    }

    return new AnimInfo(boneIds, vertexWeights);
  }

  static List<Weight> process(AIBone aiBone, int boneId) {
    List<Weight> weights = new ArrayList<>();
    int weightCount = aiBone.mNumWeights();
    for (int j = 0; j < weightCount; j++) {
      Weight weight = process(boneId, aiBone.mWeights(), j);
      weights.add(weight);
    }
    return weights;
  }

  static Weight process(int boneId, AIVertexWeight.Buffer aiWeights, int weightId) {
    AIVertexWeight aiWeight = aiWeights.get(weightId);

    return new Weight(boneId, aiWeight.mVertexId(), aiWeight.mWeight());
  }
}
