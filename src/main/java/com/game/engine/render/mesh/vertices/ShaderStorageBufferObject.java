package com.game.engine.render.mesh.vertices;

import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

@Accessors(fluent = true)
public class ShaderStorageBufferObject extends VertexBufferObject {
  protected final int binding;
  protected int size;

  public ShaderStorageBufferObject(int binding) {
    super(GL46.GL_SHADER_STORAGE_BUFFER);
    this.binding = binding;
    this.size = 0;
  }

  public float[] pull() {
    float[] results = new float[size];
    GL46.glGetNamedBufferSubData(glId, 0, results);
    return results;
  }

  public void storage(float... data) {
    GL46.glBufferStorage(target, data, GL46.GL_DYNAMIC_STORAGE_BIT);
    this.size = data.length;
  }

  public void base() {
    GL46.glBindBufferBase(target, binding, glId);
  }
}
