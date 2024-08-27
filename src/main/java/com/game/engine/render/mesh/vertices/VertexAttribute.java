package com.game.engine.render.mesh.vertices;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.opengl.GL46;

@Slf4j
@Accessors(fluent = true)
public record VertexAttribute(String key, int location, int size, int offset, int divisor) {
  public static int glBytes(int glType) {
    return glType == GL46.GL_FLOAT ? Float.BYTES : Integer.BYTES;
  }

  public void point(int stride, int glType) {
    point(stride, glType, location);
  }

  public void point(int stride, int glType, int location) {
    int glBytes = glBytes(glType);

    int glStride = stride * glBytes;
    long glOffset = (long) offset * glBytes;

    GL46.glVertexAttribPointer(
      location,
      size,
      glType,
      false,
      glStride,
      glOffset
    );

    GL46.glVertexBindingDivisor(location, divisor);
  }

  public void enable() {
    GL46.glEnableVertexAttribArray(location);
  }

  public void disable() {
    GL46.glDisableVertexAttribArray(location);
  }
}
