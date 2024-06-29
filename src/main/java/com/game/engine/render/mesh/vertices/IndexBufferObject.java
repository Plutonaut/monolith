package com.game.engine.render.mesh.vertices;

import org.lwjgl.opengl.GL46;

public class IndexBufferObject extends VertexBufferObject {
  public IndexBufferObject() {
    super(GL46.GL_ELEMENT_ARRAY_BUFFER);
  }
}
