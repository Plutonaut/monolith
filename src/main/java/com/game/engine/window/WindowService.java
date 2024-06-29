package com.game.engine.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.util.Objects;

public class WindowService {
  public EngineGLVersion initializeWindow(boolean debugMode, boolean safeMode) {
    if (!GLFW.glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW");
    EngineGLVersion glVersion = getCurrentContextVersion(safeMode);
    GLFW.glfwDefaultWindowHints();
    if (debugMode) GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GL46.GL_TRUE);
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL46.GL_FALSE);
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL46.GL_TRUE);
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, glVersion.x());
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, glVersion.y());
    if (glVersion.openCompatibility()) GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
    else {
      GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
      if (glVersion.forwardCompatibility()) GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL46.GL_TRUE);
    }
    return glVersion;
  }

  EngineGLVersion getCurrentContextVersion(boolean safeMode) {
    if (safeMode)
      return new EngineGLVersion(4, 6);
    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL46.GL_FALSE);
    final long temp = GLFW.glfwCreateWindow(1, 1, "", MemoryUtil.NULL, MemoryUtil.NULL);
    GLFW.glfwMakeContextCurrent(temp);
    GL.createCapabilities();
    final String versionStr = Objects.requireNonNull(GL46.glGetString(GL46.GL_VERSION)).split(" ")[0];
    GLFW.glfwDestroyWindow(temp);
    return EngineGLVersion.toGLVersion(versionStr);
  }
}
