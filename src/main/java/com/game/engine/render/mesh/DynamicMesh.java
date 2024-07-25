package com.game.engine.render.mesh;

import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.mesh.vertices.VertexInfo;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class DynamicMesh extends Mesh {
  private final HashMap<String, VertexBufferObject> vboAttributeKeyMap;

  public DynamicMesh(String name) {
    super(name);

    vboAttributeKeyMap = new HashMap<>();
  }

  public void redraw(MeshInfo info, IVertexCallback callback) {
    bind();
    info.vertices().forEach(vertex -> {
      updateVbo(vertex);
      if (callback != null) callback.onComplete(vertex);
    });
    if (isComplex()) updateIndexVbo(info.indices().asIntArray());
    unbind();
  }

  void updateIndexVbo(int[] values) {
    IndexBufferObject vbo = (IndexBufferObject) vboAttributeKeyMap.computeIfAbsent("indices",
                                                                                   (key) -> initializeIndexVbo()
    );
    updateIndexVboData(vbo, values);
  }

  IndexBufferObject initializeIndexVbo() {
    IndexBufferObject indexVbo = new IndexBufferObject();
    vbos().add(indexVbo);
    return indexVbo;
  }

  void updateIndexVboData(
    IndexBufferObject indexVbo, int[] values
  ) {
    IntBuffer buffer = MemoryUtil.memAllocInt(values.length).put(values).flip();
    indexVbo.bind();
    indexVbo.upload((long) values.length * GL46.GL_INT, GL46.GL_DYNAMIC_DRAW);
    indexVbo.bind();
    indexVbo.subUpload(buffer, 0);
    MemoryUtil.memFree(buffer);
  }

  void updateVbo(VertexInfo info) {
    VertexBufferObject vbo = vboAttributeKeyMap.computeIfAbsent(info.getAttributeKey(),
                                                                (key) -> initializeVbo()
    );
    updateVboData(vbo, info.vertices().asArray());
  }

  VertexBufferObject initializeVbo() {
    VertexBufferObject vbo = new VertexBufferObject();
    vbos().add(vbo);
    return vbo;
  }

  void updateVboData(VertexBufferObject vbo, float[] values) {
    FloatBuffer buffer = MemoryUtil.memAllocFloat(values.length).put(values).flip();
    vbo.bind();
    vbo.upload((long) values.length * GL46.GL_FLOAT, GL46.GL_DYNAMIC_DRAW);
    vbo.subUpload(buffer, 0);
    MemoryUtil.memFree(buffer);
  }
}
