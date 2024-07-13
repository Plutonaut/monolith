package com.game.engine.render.mesh.vertices;

import org.lwjgl.opengl.GL46;

public class IndexBufferObject extends VertexBufferObject {
  public IndexBufferObject() {
    super(GL46.GL_ELEMENT_ARRAY_BUFFER);
  }

  public void buffer(int[] values) {
    super.buffer(values, GL46.GL_STATIC_DRAW);
  }
}
