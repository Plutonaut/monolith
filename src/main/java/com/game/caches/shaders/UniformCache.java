package com.game.caches.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class UniformCache extends AbstractShaderCache {
  public UniformCache(int programId) {
    super(programId);
  }

  @Override
  protected int glLocation(String key) {
    int location = GL46.glGetUniformLocation(programId, key);

    return check(location, "Uniform", key);
  }

  public void set(String uniform, boolean value) {
    set(uniform, value ? 1 : 0);
  }

  public void set(String uniform, int value) {
    int location = get(uniform);
    if (location >= 0)
      GL46.glUniform1i(location, value);
  }

  public void set(String uniform, float value) {
    final int location = get(uniform);
    if (location >= 0)
      GL46.glUniform1f(location, value);
  }

  public void set(String uniform, Vector3f value) {
    final int location = get(uniform);
    try (MemoryStack stack = MemoryStack.stackPush()) {
      final FloatBuffer buffer = stack.mallocFloat(3);
      value.get(buffer);
      if (location >= 0)
        GL46.glUniform3fv(location, buffer);
    }
  }

  public void set(String uniform, Vector4f value) {
    final int location = get(uniform);
    try (MemoryStack stack = MemoryStack.stackPush()) {
      final FloatBuffer buffer = stack.mallocFloat(4);
      value.get(buffer);
      if (location >= 0)
        GL46.glUniform4fv(location, buffer);
    }
  }

  public void set(String uniform, Matrix4f... values) {
    if (values == null || values.length == 0) return;

    int length = values.length;
    int location = get(uniform);

    try (MemoryStack stack = MemoryStack.stackPush()) {
      FloatBuffer buffer = stack.mallocFloat(16 * values.length);
      for (int i = 0; i < length; i++) {
        values[i].get(16 * i, buffer);
      }
      if (location >= 0)
        GL46.glUniformMatrix4fv(location, false, buffer);
    }
  }
}
