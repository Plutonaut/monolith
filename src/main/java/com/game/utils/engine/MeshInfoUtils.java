package com.game.utils.engine;

import com.game.engine.physics.Bounds3D;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.EAttribute;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.opengl.GL46;

import java.nio.IntBuffer;

public class MeshInfoUtils {
  public static final int MAX_VERTEX_DATA = 9;

  public static Bounds3D calculateBounds(VertexInfo info) {
    Vector3f min = new Vector3f(Float.MAX_VALUE);
    Vector3f max = new Vector3f(Float.MIN_VALUE);

    int vertexCount = info.totalVertexCount();

    for (int i = 0; i < vertexCount; i++) {
      float[] values = info.getAttributeVertex(i, EAttribute.POS.getValue());
      float x = values[0];
      float y = values[1];
      float z = values[2];
      Vector3f vertex = new Vector3f(x, y, z);
      min.min(vertex);
      max.max(vertex);
    }

    return new Bounds3D(new Vector3f(), min, max);
  }

  public static MeshInfo process(AIMesh aiMesh) {
    return new MeshInfoBuilder()
      .use(aiMesh.mName().dataString())
      .positions(process(aiMesh.mVertices()))
      .normals(process(aiMesh.mNormals()))
      .textureCoordinates(process(
        aiMesh.mTextureCoords(0),
        2
      ))
      .vertices(process(
        aiMesh.mTangents(),
        2,
        EAttribute.TAN.getValue()
      ))
      .vertices(process(
        aiMesh.mBitangents(),
        3,
        EAttribute.BTA.getValue()
      ))
      .indices(process(
        aiMesh.mFaces(),
        aiMesh.mNumFaces()
      )).build();
  }

  static ValueStore process(AIVector3D.Buffer buffer) {
    return process(buffer, 3);
  }

  static ValueStore process(AIVector3D.Buffer buffer, int size) {
    if (buffer == null || size == 0) return null;

    ValueStore vertices = new ValueStore();

    while (buffer.hasRemaining()) {
      vertices.add(buffer.get(), size > 2);
    }

    return vertices;
  }

  static VertexInfo process(AIVector3D.Buffer buffer, int size, String attribute) {
    ValueStore vertices = process(buffer, size);
    AttribInfo attribInfo = new AttribInfo(attribute, size, 1, 0);

    return new VertexInfo(vertices, GL46.GL_FLOAT, GL46.GL_STATIC_DRAW, attribInfo);
  }

  static ValueStore process(AIFace.Buffer aiFaces, int count) {
    ValueStore indices = new ValueStore();

    for (int i = 0; i < count; i++) {
      IntBuffer buffer = aiFaces.get(i).mIndices();

      while (buffer.hasRemaining()) indices.add(buffer.get());
    }

    return indices;
  }
}
