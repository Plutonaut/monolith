package com.game.utils.math;

import com.game.utils.application.values.ValueStore;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIMatrix4x4;

public class MatrixUtils {
  public static ValueStore createInstanceMatrices(int instances) {
    return createInstanceMatrices(instances, new Vector3f(), 1);
  }

  public static ValueStore createInstanceMatrices(int instances, Vector3f offset, float scale) {
    ValueStore store = new ValueStore();
    Matrix4f mat = new Matrix4f();
    int halfInstances = instances / 2;
    Vector3f position = new Vector3f();
    for (int i = 0; i < instances; i++) {
      float pos = (i - halfInstances) / (float) halfInstances;
      position.set(pos, 0, pos);
      if (offset != null) position.add(offset);
      store.add(mat.identity().translate(position).scale(scale));
    }

    return store;
  }

  public static Matrix4f convert(AIMatrix4x4 aiMatrix) {
    final Matrix4f result = new Matrix4f();
    result.m00(aiMatrix.a1());
    result.m10(aiMatrix.a2());
    result.m20(aiMatrix.a3());
    result.m30(aiMatrix.a4());
    result.m01(aiMatrix.b1());
    result.m11(aiMatrix.b2());
    result.m21(aiMatrix.b3());
    result.m31(aiMatrix.b4());
    result.m02(aiMatrix.c1());
    result.m12(aiMatrix.c2());
    result.m22(aiMatrix.c3());
    result.m32(aiMatrix.c4());
    result.m03(aiMatrix.d1());
    result.m13(aiMatrix.d2());
    result.m23(aiMatrix.d3());
    result.m33(aiMatrix.d4());

    return result;
  }
}
