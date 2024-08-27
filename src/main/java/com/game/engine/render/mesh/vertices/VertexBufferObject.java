package com.game.engine.render.mesh.vertices;

import com.game.graphics.IGL;
import com.game.graphics.shaders.Program;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Accessors(fluent = true)
@Data
public class VertexBufferObject implements IGL {
  protected final int target;
  protected final int glId;
  protected VertexAttributeArray attributes;

  public VertexBufferObject() {
    this(GL46.GL_ARRAY_BUFFER);
  }

  public VertexBufferObject(int target) {
    this.target = target;

    glId = GL46.glGenBuffers();
  }

  public void enable() {
    if (attributes != null) attributes.enable();
  }

  public void point(Program program) {
    if (attributes != null) attributes.point(program);
  }

  public void point(String attribute, Program program) {
    if (attributes != null) attributes.point(attribute, program);
  }

  public void disable() {
    if (attributes != null) attributes.disable();
  }

  public void buffer(float[] values, int usage) {
    FloatBuffer buffer = MemoryUtil.memAllocFloat(values.length).put(values).flip();
    bind();
    upload(buffer, usage);
    MemoryUtil.memFree(buffer);
  }

  public void buffer(int[] values, int usage) {
    IntBuffer buffer = MemoryUtil.memAllocInt(values.length).put(values).flip();
    bind();
    upload(buffer, usage);
    MemoryUtil.memFree(buffer);
  }

  public void upload(FloatBuffer buffer, int usage) {
    GL46.glBufferData(target, buffer, usage);
  }

  public void upload(IntBuffer buffer, int usage) {
    GL46.glBufferData(target, buffer, usage);
  }

  public void upload(long size, int usage) { GL46.glBufferData(target, size, usage); }

  public void subUpload(FloatBuffer buffer, int offset) {
    GL46.glBufferSubData(target, offset, buffer);
  }

  public void subUpload(IntBuffer buffer, int offset) {
    GL46.glBufferSubData(target, offset, buffer);
  }

  public void bind() {
    GL46.glBindBuffer(target, glId);
  }

  public void dispose() {
    GL46.glDeleteBuffers(glId);
  }
}
