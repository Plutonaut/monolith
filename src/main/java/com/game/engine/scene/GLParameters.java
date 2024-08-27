package com.game.engine.scene;

import com.game.utils.enums.EGLParam;
import org.lwjgl.opengl.GL46;

import java.util.EnumSet;

public class GLParameters {
  protected int activeFlags;

  public void toggleParameters(int flags) {
    if (activeFlags == flags) return;

    EnumSet.allOf(EGLParam.class).forEach(param -> {
      int parameter = param.value();
      // First, check to see if flag is already active.
      boolean isActive = (activeFlags & parameter) == parameter;
      // Next, check to see whether flag is being enabled or disabled.
      boolean enabled = (flags & parameter) == parameter;
      // XOR - Enable if disabled, or disable if enabled, ignore otherwise.
      if (isActive ^ enabled) toggleParameter(param, enabled);
    });

    activeFlags = flags;
  }

  void toggleParameter(EGLParam parameter, boolean enabled) {
    getFn(parameter).glOption(enabled);
  }

  IGLRenderOptionFn getFn(EGLParam parameter) {
    return switch (parameter) {
      case CULL -> this::cull;
      case BLEND -> this::blend;
      case DEPTH -> this::depth;
      case WIREFRAME -> this::wireframe;
      case DISCARD_RAST -> this::rasterizerDiscard;
    };
  }

  void wireframe(boolean enabled) {
    int mode = enabled ? GL46.GL_LINE : GL46.GL_FILL;
    GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, mode);
  }

  void rasterizerDiscard(boolean enabled) {
    toggleGl(GL46.GL_RASTERIZER_DISCARD, enabled);
  }

  // Support for culling back faces
  void cull(boolean enabled) {
    GL46.glCullFace(GL46.GL_BACK);
    toggleGl(GL46.GL_CULL_FACE, enabled);
  }

  void depth(boolean enabled) {
    toggleGl(GL46.GL_DEPTH_TEST, enabled);
  }

  // Support for transparencies
  void blend(boolean enabled) {
    toggleGl(GL46.GL_BLEND, enabled);
    GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
  }

  private void toggleGl(int target, boolean toggle) {
    if (toggle) GL46.glEnable(target);
    else GL46.glDisable(target);
  }

  public interface IGLRenderOptionFn {
    void glOption(boolean enabled);
  }
}
