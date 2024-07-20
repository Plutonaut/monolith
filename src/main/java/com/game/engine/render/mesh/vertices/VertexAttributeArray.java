package com.game.engine.render.mesh.vertices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
public class VertexAttributeArray {
  private final String key;
  private final int size;
  private final int stride;
  private final int offset;
  private final int glType;
  private int instances;

  public boolean isInstanced() { return instances > 1; }

  public int glStride() { return stride * glBytes() * instances; }

  public long glOffset(int index) {
    if (!isInstanced()) return 0;
    long position = (long) size * index;
    long glOffset = (long) offset * glBytes();
    return position + glOffset;
  }

  protected int glBytes() {
    return glType == GL46.GL_FLOAT ? Float.BYTES : Integer.BYTES;
  }
}
