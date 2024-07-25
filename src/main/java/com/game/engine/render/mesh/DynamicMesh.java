package com.game.engine.render.mesh;

import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.mesh.vertices.VertexInfo;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class DynamicMesh extends Mesh {
  private final HashMap<String, VertexBufferObject> vboAttributeKeyMap;
  boolean initialized = false;

  public DynamicMesh(String name) {
    super(name);

    vboAttributeKeyMap = new HashMap<>();
  }

  public void redraw(MeshInfo info, IVertexCallback callback) {
    bind();
    try (MemoryStack stack = MemoryStack.stackPush()) {
      info.vertices().forEach(vertex -> {
        updateVbo(vertex, stack);
        if (callback != null) callback.onComplete(vertex);
      });
      if (isComplex()) updateVbo(info.indices().asIntArray(), stack);
    }
    unbind();
    initialized = true;
  }

  void updateVbo(int[] values, MemoryStack stack) {
    IndexBufferObject vbo = (IndexBufferObject) vboAttributeKeyMap.computeIfAbsent("indices",
                                                                                   (key) -> initializeIndexVbo()
    );
    updateIndexVboData(vbo, values, stack);
  }

  IndexBufferObject initializeIndexVbo() {
    IndexBufferObject indexVbo = new IndexBufferObject();
    vbos().add(indexVbo);
    return indexVbo;
  }

  void updateIndexVboData(
    IndexBufferObject indexVbo, int[] values, MemoryStack stack
  ) {
    IntBuffer buffer = stack.ints(values);
    int size = values.length;
    indexVbo.bind();
    indexVbo.upload((long) values.length * Integer.BYTES, GL46.GL_DYNAMIC_DRAW);
    indexVbo.subUpload(buffer, 0);

    if (vertexCount != size) vertexCount = size;
  }

  void updateVbo(VertexInfo info, MemoryStack stack) {
    VertexBufferObject vbo = vboAttributeKeyMap.computeIfAbsent(info.getAttributeKey(),
                                                                (key) -> initializeVbo()
    );
    updateVboData(vbo, info.vertices().asArray(), stack);
  }

  VertexBufferObject initializeVbo() {
    VertexBufferObject vbo = new VertexBufferObject();
    vbos().add(vbo);
    return vbo;
  }

  void updateVboData(VertexBufferObject vbo, float[] values, MemoryStack stack) {
    FloatBuffer buffer = stack.floats(values);
    vbo.bind();
    vbo.upload((long) values.length * Float.BYTES, GL46.GL_DYNAMIC_DRAW);
    vbo.subUpload(buffer, 0);
  }
}
