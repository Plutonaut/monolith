package com.game.engine.render.mesh.vertices;

import com.game.graphics.IGL;
import com.game.utils.application.IteratorUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

@Accessors(fluent = true)
@Getter
public class TransformFeedbackBuffer implements IGL {
  protected final ArrayList<Integer> vboIds;
  protected final int glId;

  public TransformFeedbackBuffer() {
    glId = GL46.glGenTransformFeedbacks();

    vboIds = new ArrayList<>();
  }

  @Override
  public void bind() {
    GL46.glBindTransformFeedback(GL46.GL_TRANSFORM_FEEDBACK, glId);
  }

  @Override
  public void dispose() {
    GL46.glDeleteTransformFeedbacks(glId);
  }

  public void base() {
    int vboCount = vboIds.size();
    for (int i = 0; i < vboCount; i++) base(i, vboIds.get(i));
  }

  public int indexOfVBO(int vboId) {
    return IteratorUtils.getIndexOf(vboIds, vboId);
  }

  public void base(int vboId) {
    int index = indexOfVBO(vboId);
    if (index < 0) {
      index = vboIds.size();
      vboIds.add(vboId);
    }
    base(vboId, index);
  }

  public void base(int vboId, int index) {
    GL46.glBindBufferBase(GL46.GL_TRANSFORM_FEEDBACK_BUFFER, index, vboId);
  }

  /**
   * GL_POINTS - the draw call topology must also be GL_POINTS. GL_LINES - the draw call topology
   * must be GL_LINES, GL_LINE_LOOP or GL_LINE_STRIP. GL_TRIANGLES - the draw call topology must be
   * GL_TRIANGLES, GL_TRIANGLE_STRIP or GL_TRIANGLE_FAN.
   *
   * @param mode
   *   primitive draw mode
   */
  public void begin(int mode) {
    GL46.glBeginTransformFeedback(mode);
  }

  public void draw(int mode) {
    GL46.glDrawTransformFeedback(mode, glId);
  }

  public void end() {
    GL46.glEndTransformFeedback();
  }
}
