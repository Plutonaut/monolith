package com.game.caches.shaders;

import org.lwjgl.opengl.GL46;

public class AttributeCache extends AbstractShaderCache {
  public AttributeCache(int programId) {
    super(programId);
  }

  @Override
  protected int glLocation(String key) {
    int location = GL46.glGetAttribLocation(programId, key);
    return check(location, "Attribute", key);
  }

  public void point(String attribute, int size, int stride, int offset, int glType, int instances) {
    int location = get(attribute);
    boolean instanced = instances > 1;
    int glBytes = glBytes(glType);
    for (int i = 0; i < instances; i++) {
      location += i;
      GL46.glVertexAttribPointer(location, size, glType, false, stride * glBytes * instances, (long) offset * glBytes);
      if (instanced) GL46.glVertexBindingDivisor(location, 1);
      offset += size;
    }
  }

  public int glBytes(int glType) {
    return glType == GL46.GL_FLOAT ? Float.BYTES : Integer.BYTES;
  }

  public void enable(String key) {
    int location = get(key);
    if (location >= 0) GL46.glEnableVertexAttribArray(location);
  }

  public void disable(String key) {
    int location = get(key);
    if (location >= 0) GL46.glDisableVertexAttribArray(location);
  }
}
