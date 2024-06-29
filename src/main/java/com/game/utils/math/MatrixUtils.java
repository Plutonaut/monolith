package com.game.utils.math;

import org.joml.Matrix4f;
import org.lwjgl.assimp.AIMatrix4x4;

public class MatrixUtils {
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
