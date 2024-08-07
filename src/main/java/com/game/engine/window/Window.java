package com.game.engine.window;

import com.game.engine.controls.JoyStick;
import com.game.engine.controls.Keyboard;
import com.game.engine.controls.Mouse;
import com.game.utils.logging.LogPrintStreamFactory;
import com.game.utils.logging.LoggingUtils;
import com.game.utils.math.VectorUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.PrintStream;
import java.nio.IntBuffer;

@Accessors(fluent = true)
@Data
@Slf4j
public class Window {
  private final long handle;
  private final Mouse mouse;
  private final Keyboard keyboard;
  private final JoyStick joystick;
  private Callback debugCallback;
  private PrintStream printStream;
  private String title;
  private int width;
  private int height;

  public Window(
    int width, int height, String title, boolean debugMode, boolean safeMode, boolean vSync
  ) {
    // Creating an instance of WindowUtils rather than using a static class for thread safety.
    EngineGLVersion glVersion = new WindowService().initializeWindow(debugMode, safeMode);
    this.title = String.format("%s OpenGL %s", title, glVersion);
    handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
    if (handle == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");
    mouse = new Mouse();
    joystick = new JoyStick();
    keyboard = new Keyboard();
    setupCallbacks();
    GLFW.glfwMakeContextCurrent(handle);
    GLFW.glfwSwapInterval(vSync ? 1 : 0);
    GLFW.glfwShowWindow(handle);
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.callocInt(1);
      IntBuffer h = stack.callocInt(1);
      GLFW.glfwGetFramebufferSize(handle, w, h);
      this.width = w.get(0);
      this.height = h.get(0);
    }
    GL.createCapabilities();
    // debug
    if (debugMode) {
      printStream = LogPrintStreamFactory.create(
        LoggingUtils.WINDOW_LOG_FILENAME,
        LoggingUtils.WINDOW_LOG_DIRECTORY,
        ".log",
        5,
        true
      );
      debugCallback = GLUtil.setupDebugMessageCallback(printStream);
    }
  }

  public void title(String title) {
    GLFW.glfwSetWindowTitle(handle, this.title + " - " + title);
  }

  public void clear() {
    GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
  }

  public void clearColor(float red, float green, float blue, float alpha) {
    GL46.glClearColor(red, green, blue, alpha);
  }

  public void viewport() { viewport(0, 0); }

  public void viewport(int x, int y) {
    GL46.glViewport(x, y, width, height);
  }

  public void update() { GLFW.glfwSwapBuffers(handle); }

  public void poll() { GLFW.glfwPollEvents(); }

  public void maximize() { GLFW.glfwMaximizeWindow(handle); }

  public void restore() { GLFW.glfwRestoreWindow(handle); }

  public float ratio() { return (float) width / height; }

  public boolean windowShouldClose() { return GLFW.glfwWindowShouldClose(handle); }

  public void setWindowShouldClose(boolean close) { GLFW.glfwSetWindowShouldClose(handle, close); }

  public Vector2f getPointInScreenSpace(Vector3f point) {
    return VectorUtils.screenSpace(point, width, height);
  }

  // TODO: Triggers too often. May need to wrap in a timeout.
  public boolean isKeyPressed(int keyCode) {
    return GLFW.glfwGetKey(handle, keyCode) == GLFW.GLFW_PRESS;
  }

  // Set camera position z = -1f representing the lowest possible value of z
  public Vector3f normalizedDeviceSpace(Vector2f point) {
    float x = 2f * point.x / width - 1f;
    float y = 1 - 2f * point.y / height;
    return new Vector3f(x, y, -1f);
  }

  public Vector2f screenSpace(Vector3f point) {
    float x = (point.x + point.x * 0.5f) * width;
    float y = (point.y - point.y * 0.5f) * height;

    return new Vector2f(x, y);
  }

  void setupCallbacks() {
    GLFW.glfwSetFramebufferSizeCallback(handle, (win, w, h) -> {
      width = w;
      height = h;
    });

    GLFW.glfwSetErrorCallback((e, m) -> {
      log.error("Error code [{}], message [{}]", e, MemoryUtil.memUTF8(m));
    });

    GLFW.glfwSetKeyCallback(handle, keyboard::onKeyPress);
    GLFW.glfwSetCursorPosCallback(handle, mouse::onPositionChanged);
    GLFW.glfwSetMouseButtonCallback(handle, mouse::onClick);
    GLFW.glfwSetCursorEnterCallback(handle, mouse::onCursorEnter);

    joystick.setupCallback();
  }

  public void dispose() {
    if (!windowShouldClose()) setWindowShouldClose(true);

    Callbacks.glfwFreeCallbacks(handle);
    GLFW.glfwDestroyWindow(handle);
    joystick.dispose();
    if (debugCallback != null) debugCallback.free();
    if (printStream != null) printStream.close();
    GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
    if (callback != null) callback.free();
    GLFW.glfwTerminate();
  }
}
