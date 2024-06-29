package com.game.utils.engine;

import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.utils.application.ValueStore;
import com.game.utils.enums.EAttribute;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.opengl.GL46;

import java.nio.IntBuffer;

public class MeshInfoUtils {
  public static MeshInfo process(AIMesh aiMesh) {
    return new MeshInfo(aiMesh.mName().dataString()).addVertices(process(aiMesh.mVertices(),
                                                                         3,
                                                                         EAttribute.POS.getValue()))
                                                    .addVertices(process(aiMesh.mNormals(),
                                                                         3,
                                                                         EAttribute.NRM.getValue()))
                                                    .addVertices(process(aiMesh.mTextureCoords(0),
                                                                         2,
                                                                         EAttribute.TXC.getValue()))
                                                    .addVertices(process(aiMesh.mTangents(),
                                                                         2,
                                                                         EAttribute.TAN.getValue()))
                                                    .addVertices(process(aiMesh.mBitangents(),
                                                                         3,
                                                                         EAttribute.BTA.getValue()))
                                                    .setIndices(process(aiMesh.mFaces(),
                                                                        aiMesh.mNumFaces()));
  }

  static VertexInfo process(AIVector3D.Buffer buffer, int size, String attribute) {
    if (buffer == null || size == 0) return null;

    ValueStore vertices = new ValueStore();

    while (buffer.hasRemaining()) {
      vertices.add(buffer.get(), size > 2);
    }
    AttribInfo attribInfo = new AttribInfo(attribute, size, 1);

    return new VertexInfo(vertices, GL46.GL_FLOAT, attribInfo);
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
