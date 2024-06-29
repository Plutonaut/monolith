package com.game.engine.render.mesh.vertices;

import com.game.graphics.IGL;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Accessors(fluent = true)
@Data
public class VertexBufferObject implements IGL {
  protected final int target;
  protected final int glId;

  public VertexBufferObject() {
    this(GL46.GL_ARRAY_BUFFER);
  }

  public VertexBufferObject(int target) {
    this.target = target;

    glId = GL46.glGenBuffers();
  }

  public void upload(FloatBuffer buffer, int usage) {
    GL46.glBufferData(target, buffer, usage);
  }

  public void upload(IntBuffer buffer, int usage) {
    GL46.glBufferData(target, buffer, usage);
  }

  public void bind() {
    GL46.glBindBuffer(target, glId);
  }

  public void dispose() {
    GL46.glDeleteBuffers(glId);
  }
}
